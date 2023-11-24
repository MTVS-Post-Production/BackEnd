package com.alal.backend.controller.user;

import com.alal.backend.domain.dto.request.UpdateAvatarRequest;
import com.alal.backend.domain.dto.request.UploadMemoRequest;
import com.alal.backend.domain.dto.request.UploadProjectRequest;
import com.alal.backend.domain.dto.response.*;
import com.alal.backend.service.user.AvatarService;
import com.alal.backend.service.user.GroupService;
import lombok.RequiredArgsConstructor;
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
    private final AvatarService avatarService;

    @PostMapping("/memo/upload")
    public ResponseEntity<UploadMemoResponse> upload(@RequestBody UploadMemoRequest uploadMemoRequest
//    ,@CurrentUser UserPrincipal userPrincipal
    ) {
        Long userId = 1L;
//        Long userId = userPrincipal.getId();
        return ResponseEntity.ok(groupService.uploadMemo(uploadMemoRequest, userId));
    }

    @GetMapping("/memo")
    public ResponseEntity<ReadMemoResponse> read(
//            @CurrentUser UserPrincipal userPrincipal
    ) {
        Long userId = 1L;
//        Long userId = userPrincipal.getId();
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
        long start = System.nanoTime();
        ReadProjectResponse readProjectResponse = groupService.readProject(projectId);
        long end = System.nanoTime();
        System.out.println("수행시간: " + (end - start) + " ns");
        return ResponseEntity.ok(readProjectResponse);
    }

    @PatchMapping("/avatar")
    public ResponseEntity<UpdateAvatarResponse> update(@RequestBody UpdateAvatarRequest avatarRequest) {
        return ResponseEntity.ok(avatarService.updateAvatar(avatarRequest));
    }
}
