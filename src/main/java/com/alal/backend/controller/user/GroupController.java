package com.alal.backend.controller.user;

import com.alal.backend.config.security.token.CurrentUser;
import com.alal.backend.config.security.token.UserPrincipal;
import com.alal.backend.domain.dto.response.*;
import com.alal.backend.domain.dto.request.UploadMemoRequest;
import com.alal.backend.domain.dto.request.UploadProjectRequest;
import com.alal.backend.service.user.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/memo/upload")
    public ResponseEntity<UploadMemoResponse> upload(@RequestBody UploadMemoRequest uploadMemoRequest
    ,@CurrentUser UserPrincipal userPrincipal
    ) {
//        Long userId = 1L;
        Long userId = userPrincipal.getId();
        return ResponseEntity.ok(groupService.uploadMemo(uploadMemoRequest, userId));
    }

    @GetMapping("/memo")
    public ResponseEntity<ReadMemoResponse> read(
            @CurrentUser UserPrincipal userPrincipal
    ) {
//        Long userId = 1L;
        Long userId = userPrincipal.getId();
        return ResponseEntity.ok(groupService.readMemos(userId));
    }

    @PostMapping("/project/upload")
    public ResponseEntity<UploadProjectResponse> uploadProject(@RequestBody UploadProjectRequest uploadProjectRequest
//    , @CurrentUser UserPrincipal userPrincipal
                                                               ) {
        Long userId = 1L;
//        Long userId = userPrincipal.getId();
        return ResponseEntity.ok(groupService.uploadProject(uploadProjectRequest, userId));
    }

    @GetMapping("/project/all")
    public ResponseEntity<List<ReadProjectsResponse>> readProjects(@PageableDefault(value = 8) Pageable pageable
//            , @CurrentUser UserPrincipal userPrincipal
            ) {
//        Long userId = userPrincipal.getId();
        Long userId = 1L;
        return ResponseEntity.ok(groupService.readProjects(userId, pageable).getContent());
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<ReadProjectResponse> readProject(@PathVariable("id") Long projectId) {
        return ResponseEntity.ok(groupService.readProject(projectId));
    }
}
