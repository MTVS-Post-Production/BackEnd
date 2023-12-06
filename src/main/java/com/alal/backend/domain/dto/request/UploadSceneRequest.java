package com.alal.backend.domain.dto.request;

import com.alal.backend.domain.entity.project.Scene;
import com.alal.backend.domain.entity.project.Script;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
public class UploadSceneRequest {
    private Long projectId;
    private Long sceneNo;
    private String story;
    private String levelPosition;
    private String thumbNail;

    public byte[] decodeBase64() {
        return Base64.getDecoder().decode(thumbNail);
    }

    public Scene from(Script script, String thumbnailUrl) {
        return Scene.builder()
                .sceneNo(sceneNo)
                .story(story)
                .levelPosition(levelPosition)
                .thumbnail(thumbnailUrl)
                .script(script)
                .build();
    }
}
