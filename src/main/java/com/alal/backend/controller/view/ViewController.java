package com.alal.backend.controller.view;

import com.alal.backend.config.security.token.CurrentUser;
import com.alal.backend.config.security.token.UserPrincipal;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.payload.request.auth.FbxRequest;
import com.alal.backend.payload.request.auth.FlaskRequest;
import com.alal.backend.payload.response.FbxResponse;
import com.alal.backend.payload.response.FlaskResponse;
import com.alal.backend.payload.response.ViewResponse;
import com.alal.backend.service.user.MotionService;
import java.net.HttpCookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/view")
@RequiredArgsConstructor
public class ViewController {

    private final MotionService motionService;

    @GetMapping
    public String main(Model model, @CurrentUser UserPrincipal userPrincipal,
                       @PageableDefault(size = 30) Pageable pageable) {
        Long userId = userPrincipal.getId();
        Page<ViewResponse> viewResponses = motionService.createViewResponse(userId, pageable);

        model.addAttribute("motionUrls", viewResponses);

        return "main/imagePage";
    }

    // 클라이언트에서 동영상 파일(mp4)을 받아 Flask 서버와 통신하여 문자열 리스트를 받음
    @PostMapping("/video")
    @ResponseBody
    public String videoPost(@RequestBody FlaskRequest flaskRequest, @CurrentUser UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        motionService.findUrlByUploadMp4(flaskRequest, userId);

        return "success";
    }

    // 클라이언트에서 음성 파일을 받아 Flask 서버와 통신 후 변조된 음성 파일 응답
    @PostMapping("/voice")
    @ResponseBody
    public ResponseEntity<FlaskResponse> voicePost(@RequestBody FlaskRequest flaskRequest
                                                   ) {
        FlaskResponse flaskResponse = motionService.uploadAndRespondWithAudioFileSuccess(flaskRequest);
        return ResponseEntity.ok(flaskResponse);
    }

    // 클라이언트에서 문자열을 받아 Flask 서버와 통신하여 문자열 리스트 반환받음
//    @PostMapping("/message")
//    public String messagePost(@RequestBody String message, Model model,
//                              @PageableDefault(size = 30) Pageable pageable){
//        Page<ViewResponse> viewResponse = motionService.findGifByMessages(message, pageable);
//
//        model.addAttribute("motionUrls", viewResponse);
//
//        return "main/imagePage";
//    }

    // 웹에서 다운로드 버튼 클릭 시 /view/result?fbxUrl=... 로 리다이렉트
    @PostMapping("/send/fbx")
    @ResponseBody
    public ResponseEntity<FbxResponse> send(@RequestBody FbxRequest fbxRequest){
        FbxResponse response = FbxResponse.fromFbxUrl(fbxRequest);
        URI redirectUri = UriComponentsBuilder.fromPath("/view/result")
                .queryParam("fbxUrl", response.getFbxUrl())
                .build()
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}