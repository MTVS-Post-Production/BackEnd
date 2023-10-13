package com.alal.backend.controller.view;

import com.alal.backend.repository.user.MotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/view")
@RequiredArgsConstructor
public class ViewController {

    @Value("${ai.model.serving.url}")
    private String flaskUrl;

    private final MotionRepository motionRepository;

    @GetMapping
    public String main() {
        return "main/imagePage";
    }

    @PostMapping("/message")
    public String messagePost(@RequestBody String message, Model model){

        // Flask 서버 통신
        //
//        String fileName = WebClient.create()
//                .post()
//                .uri(flaskUrl)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .bodyValue(message)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();

        List<String> imageUrl = motionRepository.findByMotionGifLikeFileName(message);

        model.addAttribute("imgUrl", imageUrl);

//        return ResponseEntity.ok(imageUrl);

        return "main/imagePage";
    }
}
