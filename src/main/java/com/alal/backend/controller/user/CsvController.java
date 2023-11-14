package com.alal.backend.controller.user;

import com.alal.backend.config.security.token.CurrentUser;
import com.alal.backend.config.security.token.UserPrincipal;
import com.alal.backend.domain.dto.request.ReadMemoRequest;
import com.alal.backend.domain.dto.request.UploadMemoRequest;
import com.alal.backend.domain.dto.response.ReadMemoResponse;
import com.alal.backend.domain.dto.response.UploadMemoResponse;
import com.alal.backend.service.user.CsvService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class CsvController {

    private final CsvService csvService;

    @PostMapping("/upload")
    public ResponseEntity<UploadMemoResponse> upload(@ModelAttribute UploadMemoRequest uploadMemoRequest) {
        return ResponseEntity.ok(csvService.uploadMemo(uploadMemoRequest));
    }

    @GetMapping
    public ResponseEntity<Page<ReadMemoResponse>> read(@PageableDefault(size = 30)  Pageable pageable
//            ,@CurrentUser UserPrincipal userPrincipal
    ) {
        Long userId = 1L;
        return ResponseEntity.ok(csvService.readMemos(userId, pageable));
    }
}
