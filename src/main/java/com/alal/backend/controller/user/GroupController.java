package com.alal.backend.controller.user;

import com.alal.backend.domain.dto.request.UploadMemoRequest;
import com.alal.backend.domain.dto.request.UploadProjectRequest;
import com.alal.backend.domain.dto.response.ReadMemoResponse;
import com.alal.backend.domain.dto.response.UploadMemoResponse;
import com.alal.backend.domain.dto.response.UploadProjectResponse;
import com.alal.backend.service.user.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/memo/upload")
    public ResponseEntity<UploadMemoResponse> upload(@RequestBody UploadMemoRequest uploadMemoRequest
//    ,@CurrentUser UserPrincipal userPrincipal
    ) {
        Long userId = 1L;
        return ResponseEntity.ok(groupService.uploadMemo(uploadMemoRequest, userId));
    }

    @GetMapping("/memo")
    public ResponseEntity<ReadMemoResponse> read(
//            ,@CurrentUser UserPrincipal userPrincipal
    ) {
        Long userId = 1L;
        return ResponseEntity.ok(groupService.readMemos(userId));
    }

    @PostMapping("/project/upload")
    public ResponseEntity<UploadProjectResponse> uploadProject(@RequestBody UploadProjectRequest uploadProjectRequest
//    , @CurrentUser UserPrincipal userPrincipal
                                                               ) {
        Long userId = 1L;
        return ResponseEntity.ok(groupService.uploadProject(uploadProjectRequest, userId));
    }
}
