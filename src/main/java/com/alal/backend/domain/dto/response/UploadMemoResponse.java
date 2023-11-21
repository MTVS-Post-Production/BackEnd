package com.alal.backend.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadMemoResponse {
    private final String uploadUrl;

    public static UploadMemoResponse fromEntity(String uploadUrl) {
        return UploadMemoResponse.builder()
                .uploadUrl(uploadUrl)
                .build();
    }
}