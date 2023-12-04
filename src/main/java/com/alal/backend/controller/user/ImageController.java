package com.alal.backend.controller.user;

import com.alal.backend.config.security.token.CurrentUser;
import com.alal.backend.config.security.token.UserPrincipal;
import com.alal.backend.domain.dto.request.UploadImageRequest;
import com.alal.backend.domain.dto.response.ReadImageResponse;
import com.alal.backend.domain.dto.response.UploadImageResponse;
import com.alal.backend.service.user.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("upload")
    public ResponseEntity<UploadImageResponse> upload(@RequestBody UploadImageRequest uploadImageRequest
//                                                      ,@CurrentUser UserPrincipal userPrincipal
    ) throws ExecutionException, InterruptedException {
        Long userId = 1L;
//        Long userId = userPrincipal.getId();
        return ResponseEntity.ok(imageService.uploadImage(uploadImageRequest, userId));
    }

    @GetMapping
    public ResponseEntity<Page<ReadImageResponse>> read(@PageableDefault(size = 18) Pageable pageable
//                                                  , @CurrentUser UserPrincipal userPrincipal
    ) {
        Long userId = 1L;
//        Long userId = userPrincipal.getId();
        return ResponseEntity.ok(imageService.readImages(userId, pageable));
    }
}
