package com.alal.backend.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FbxUrlResponse {
    private String fbxFileName;
    private String fbxUrl;
}
