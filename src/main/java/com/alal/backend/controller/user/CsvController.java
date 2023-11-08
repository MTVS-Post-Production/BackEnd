package com.alal.backend.controller.user;

import com.alal.backend.payload.request.user.UploadMemoRequest;
import com.alal.backend.payload.response.UploadMemoResponse;
import com.alal.backend.service.user.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class CsvController {

    private final CsvService csvService;

    @PostMapping("/upload")
    public ResponseEntity<UploadMemoResponse> upload(@ModelAttribute UploadMemoRequest uploadMemoRequest) {
        UploadMemoResponse uploadMemoResponse = csvService.uploadMemo(uploadMemoRequest);

        return ResponseEntity.ok(uploadMemoResponse);
    }
}
