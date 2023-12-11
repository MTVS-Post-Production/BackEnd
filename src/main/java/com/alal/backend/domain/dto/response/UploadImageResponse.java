package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.user.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadImageResponse {
    private String albedoUrl;
    private String meshMtl;
    private String meshObj;
    private String thumbnailUrl;

    public static UploadImageResponse fromEntity(Image createdImage) {
        return UploadImageResponse.builder()
                .albedoUrl(createdImage.getAlbedoUrl())
                .meshMtl(createdImage.getMeshMtlUrl())
                .meshObj(createdImage.getMeshObj())
                .thumbnailUrl(createdImage.getThumbnailUrl())
                .build();
    }
}
