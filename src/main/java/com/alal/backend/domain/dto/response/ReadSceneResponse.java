package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.project.Scene;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReadSceneResponse {
    private Long sceneNo;

    private String story;

    private String levelPosition;

    private String thumbnail;
}
