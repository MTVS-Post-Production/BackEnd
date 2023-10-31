package com.alal.backend.domain.dto.response;

import com.alal.backend.payload.request.auth.FbxRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FbxResponse {
    private String fbxUrl;

    public static FbxResponse fromFbxUrl(FbxRequest fbxRequest) {
        return FbxResponse.builder()
                .fbxUrl(fbxRequest.getFbxUrl())
                .build();
    }
}
