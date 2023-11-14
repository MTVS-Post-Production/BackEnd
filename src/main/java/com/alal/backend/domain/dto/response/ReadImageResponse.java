package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.user.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadImageResponse {
    private Long userId;

    private String albedoUrl;

    private String meshMtlUrl;

    private String meshObj;

    public static ReadImageResponse fromEntity(Image image) {
        return ReadImageResponse.builder()
                .albedoUrl(image.getAlbedoUrl())
                .meshMtlUrl(image.getMeshMtlUrl())
                .meshObj(image.getMeshObj())
                .build();
    }
}
