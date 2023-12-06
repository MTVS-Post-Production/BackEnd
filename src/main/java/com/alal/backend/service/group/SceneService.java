package com.alal.backend.service.group;

import com.alal.backend.domain.dto.request.UploadSceneRequest;
import com.alal.backend.domain.dto.response.ReadSceneResponse;
import com.alal.backend.domain.dto.response.ReadSceneResponseList;
import com.alal.backend.domain.dto.response.UploadSceneResponse;
import com.alal.backend.domain.entity.project.Scene;
import com.alal.backend.domain.entity.project.Script;
import com.alal.backend.repository.group.SceneRepository;
import com.alal.backend.repository.group.ScriptRepository;
import com.alal.backend.utils.event.UploadRollBackEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SceneService {
    private final SceneRepository sceneRepository;
    private final ScriptRepository scriptRepository;
    private final GoogleService googleService;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Transactional(readOnly = true)
    public ReadSceneResponseList readAllScene(Long projectId) {
        Script script = scriptRepository.getReferenceById(projectId);
        return script.toReadSceneResponseList();
    }

    @Transactional
    public UploadSceneResponse uploadScene(UploadSceneRequest uploadSceneRequest) {
        Script script = scriptRepository.getReferenceById(uploadSceneRequest.getProjectId());
        String thumbnailUrl = googleService.uploadImage(uploadSceneRequest);
        eventPublisher.publishEvent(new UploadRollBackEvent(bucketName, thumbnailUrl));
        Scene scene = uploadSceneRequest.from(script, thumbnailUrl);

        sceneRepository.save(scene);
        return scene.toUploadResponse();
    }

    @Transactional(readOnly = true)
    public ReadSceneResponse readDetailScene(Long projectId, Long sceneNo) {
        Script script = scriptRepository.getReferenceById(projectId);
        Scene scene = sceneRepository.findByScriptAndSceneNo(script, sceneNo);

        return scene.toReadSceneResponse();
    }
}
