package com.alal.backend.controller.view;

import com.alal.backend.payload.request.auth.FbxRequest;
import com.alal.backend.payload.response.FbxResponse;
import com.alal.backend.payload.response.MessageResponse;
import com.alal.backend.payload.response.ViewResponse;
import com.alal.backend.repository.user.MotionRepository;
import com.alal.backend.service.user.MotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/view")
@RequiredArgsConstructor
public class ViewController {

    @Value("${ai.model.serving.url}")
    private String flaskUrl;

    private final MotionService motionService;

    private Page<ViewResponse> lastViewResponses;

    @GetMapping
    public String main(Model model) {
        if (lastViewResponses != null) {
            model.addAttribute("motionUrls", lastViewResponses);
        }
        return "main/imagePage";
    }

    // 클라이언트에서 문자열을 받아 Flask 서버와 통신하여 문자열 리스트 반환받음
    @PostMapping("/message")
    public String messagePost(@RequestBody List<String> messages, Model model,
                              @PageableDefault(size = 30) Pageable pageable){
        // Flask 서버 통신
        // 분석한 결과를 문자열 리스트로 반환받음
        List<MessageResponse> messageResponses = WebClient.create()
                .post()
                .uri(flaskUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(messages)
                .retrieve()
                .bodyToFlux(MessageResponse.class)
                .collectList()
                .block();

        List<String> fileNames = messageResponses.stream()
                .map(MessageResponse::getMessage)
                .collect(Collectors.toList());

        Page<ViewResponse> viewResponse = motionService.findGifByMessages(fileNames, pageable);
        lastViewResponses = viewResponse;

        model.addAttribute("motionUrls", viewResponse);

        return "main/imagePage";
    }

    // 웹에서 다운로드 버튼 클릭 시, 언리얼 엔진으로 전송
    @PostMapping("/send/fbx")
    @ResponseBody
    public ResponseEntity<FbxResponse> send(@RequestBody FbxRequest fbxRequest){
        return ResponseEntity.ok(FbxResponse.fromFbxUrl(fbxRequest));
    }

}