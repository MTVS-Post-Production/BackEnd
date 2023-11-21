package com.alal.backend.controller.user;

import com.alal.backend.domain.dto.request.UploadMemoRequest;
import com.alal.backend.domain.dto.response.ReadMemoResponse;
import com.alal.backend.domain.dto.response.UploadMemoResponse;
import com.alal.backend.service.user.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/upload")
    public ResponseEntity<UploadMemoResponse> upload(@ModelAttribute UploadMemoRequest uploadMemoRequest
//    ,@CurrentUser UserPrincipal userPrincipal
    ) {
        Long userId = 1L;
        return ResponseEntity.ok(memoService.uploadMemo(uploadMemoRequest, userId));
    }

    @GetMapping
    public ResponseEntity<ReadMemoResponse> read(
//            ,@CurrentUser UserPrincipal userPrincipal
    ) {
        Long userId = 1L;
        return ResponseEntity.ok(memoService.readMemos(userId));
    }
}
