package com.alal.backend.controller.user;

import com.alal.backend.domain.dto.request.UpdateAvatarRequest;
import com.alal.backend.domain.dto.request.UploadMemoRequest;
import com.alal.backend.domain.dto.request.UploadProjectRequest;
import com.alal.backend.domain.dto.request.UploadSceneRequest;
import com.alal.backend.domain.dto.response.*;
import com.alal.backend.service.group.AvatarService;
import com.alal.backend.service.group.MemoService;
import com.alal.backend.service.group.ProjectService;
import com.alal.backend.service.group.SceneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final MemoService memoService;
    private final AvatarService avatarService;
    private final ProjectService projectService;
    private final SceneService sceneService;

    @PostMapping("/memo/upload")
    public ResponseEntity<UploadMemoResponse> upload(@RequestBody UploadMemoRequest uploadMemoRequest
//    ,@CurrentUser UserPrincipal userPrincipal
    ) {
        Long userId = 1L;
//        Long userId = userPrincipal.getId();
        return ResponseEntity.ok(memoService.uploadMemo(uploadMemoRequest, userId));
    }

    @GetMapping("/memo")
    public ResponseEntity<ReadMemoResponse> readAll(
//            @CurrentUser UserPrincipal userPrincipal
    ) {
        Long userId = 1L;
//        Long userId = userPrincipal.getId();
        return ResponseEntity.ok(memoService.readMemos(userId));
    }

    @PostMapping("/project/upload")
    public ResponseEntity<UploadProjectResponse> uploadProject(@RequestBody UploadProjectRequest uploadProjectRequest
//    , @CurrentUser UserPrincipal userPrincipal
                                                               ) {
        Long userId = 1L;
//        Long userId = userPrincipal.getId();
        return ResponseEntity.ok(projectService.uploadProject(uploadProjectRequest, userId));
    }

    @GetMapping("/project/all")
    public ResponseEntity<ReadProjectsResponseList> readProjects(@PageableDefault(value = 8) Pageable pageable
//            , @CurrentUser UserPrincipal userPrincipal
            ) {
//        Long userId = userPrincipal.getId();
        Long userId = 1L;
        return ResponseEntity.ok(memoService.readProjects(userId, pageable));
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<ReadProjectResponse> readProject(@PathVariable("id") Long projectId) throws IOException {
        ReadProjectResponse readProjectResponse = memoService.readProject(projectId);
        return ResponseEntity.ok(readProjectResponse);
    }

    @PatchMapping("/avatar")
    public ResponseEntity<UpdateAvatarResponse> update(@RequestBody UpdateAvatarRequest avatarRequest) {
        return ResponseEntity.ok(avatarService.updateAvatar(avatarRequest));
    }

    @GetMapping("/scene/{id}")
    public ResponseEntity<ReadSceneResponseList> readAll(@PathVariable("id") Long projectId) {
        return ResponseEntity.ok(sceneService.readAllScene(projectId));
    }

    @PostMapping("/scene/upload")
    public ResponseEntity<UploadSceneResponse> upload(@RequestBody UploadSceneRequest uploadSceneRequest) {
        return ResponseEntity.ok(sceneService.uploadScene(uploadSceneRequest));
    }
}
