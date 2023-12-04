package com.alal.backend.service.group;

import com.alal.backend.domain.dto.response.ReadSceneResponse;
import com.alal.backend.domain.entity.project.Scene;
import com.alal.backend.repository.group.SceneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final SceneRepository sceneRepository;

    public List<ReadSceneResponse> readAllScene(Long scriptId) {
        List<Scene> scenes = sceneRepository.findAllById(Collections.singleton(scriptId));

        return scenes.stream()
                .map(Scene::toReadSceneResponse)
                .collect(Collectors.toList());
    }
}
