package com.alal.backend.controller.view;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/view")
public class ViewController {

    @Value("${ai.model.serving.url}")
    private String flaskUrl;

    @PostMapping("/message")
    public String testPost(@RequestBody String message, Model model){
//        String fileName = WebClient.create()
//                .post()
//                .uri(flaskUrl)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .bodyValue(message)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        List<String> imageUrl = getGifsFromCloudStorage("myBucket", responseMessage);

        List<String> imageUrl = getGifsFromCloudStorage("myBucket", message);

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
