package com.alal.backend.controller.common;

import com.alal.backend.dto.response.ResponseTestToken;
import com.alal.backend.service.auth.AuthService;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    private final AuthService authService;

    @PostMapping
    public String testPost(@RequestBody String message, Model model){
        String responseMessage = WebClient.create()
                .post()
                .uri("192.1.6.8:8080/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(message)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<String> imageUrl = getGifsFromCloudStorage("myBucket", responseMessage);

        model.addAttribute("imgUrl", imageUrl);

        return "imageDisplay";
    }

    public List<String> getGifsFromCloudStorage(String bucketName, String fileName) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Bucket bucket = storage.get(bucketName);
        Page<Blob> blobs = bucket.list();

        List<String> gifUrls = new ArrayList<>();

        for (Blob blob : blobs.iterateAll()) {
            if (blob.getName().startsWith(fileName)) {
                gifUrls.add(blob.getSelfLink());
            }
        }

        return gifUrls;
    }
}
