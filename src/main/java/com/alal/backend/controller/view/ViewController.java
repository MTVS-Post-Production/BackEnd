package com.alal.backend.controller.view;

import com.alal.backend.payload.request.auth.FbxRequest;
import com.alal.backend.payload.response.FbxResponse;
import com.alal.backend.payload.response.FlaskResponse;
import com.alal.backend.payload.response.ViewResponse;
import com.alal.backend.service.user.MotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    private Page<ViewResponse> lastViewResponses;

    @GetMapping
    public String main(Model model) {
        if (lastViewResponses != null) {
            model.addAttribute("motionUrls", lastViewResponses);
        }
        return "main/imagePage";
    }

    // 클라이언트에서 동영상 파일(mp4)을 받아 Flask 서버와 통신하여 문자열 리스트를 받음
    @PostMapping("/video")
    public String videoPost(@RequestParam("file") MultipartFile file,
                            @PageableDefault(size = 30) Pageable pageable,
                            Model model
                            ) {
        Page<ViewResponse> viewResponse = motionService.findUrlByUploadMp4(file, pageable);
        lastViewResponses = viewResponse;

        model.addAttribute("motionUrls", viewResponse);

        return "main/imagePage";
    }

    // 클라이언트에서 음성 파일을 받아 Flask 서버와 통신 후 변조된 음성 파일 응답
    @PostMapping("/voice")
    @ResponseBody
    public ResponseEntity<FlaskResponse> voicePost(@RequestParam("file") MultipartFile file
    ) {
        FlaskResponse flaskResponse = motionService.uploadAndRespondWithAudioFileSuccess(file);
        return ResponseEntity.ok(flaskResponse);
    }

    // 클라이언트에서 문자열을 받아 Flask 서버와 통신하여 문자열 리스트 반환받음
    @PostMapping("/message")
    public String messagePost(@RequestBody String message, Model model,
                              @PageableDefault(size = 30) Pageable pageable){
        Page<ViewResponse> viewResponse = motionService.findGifByMessages(message, pageable);
        lastViewResponses = viewResponse;

        model.addAttribute("motionUrls", viewResponse);

        return "main/imagePage";
    }

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

    // 다운로드 버튼을 통해 리다이렉트한 주소에 대한 GET 요청
    @GetMapping("/result")
    public String result(@RequestParam("fbxUrl") String fbxUrl, Model model) {
        model.addAttribute("fbxUrl", fbxUrl);
        return "main/result";
    }



    // 웹에서 다운로드 버튼 클릭 시, 언리얼 엔진으로 전송
//    @PostMapping("/send/fbx")
//    @ResponseBody
//    public ResponseEntity<FbxResponse> send(@RequestBody FbxRequest fbxRequest){
//        return ResponseEntity.ok(FbxResponse.fromFbxUrl(fbxRequest));
//    }

}