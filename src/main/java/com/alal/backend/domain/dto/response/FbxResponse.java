package com.alal.backend.domain.dto.response;

import com.alal.backend.payload.request.auth.FbxRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FbxResponse {
    private String fbxUrl;
    private String gifUrl;
    private String title;

    public static FbxResponse fromFbxUrl(FbxRequest fbxRequest) {
        return FbxResponse.builder()
                .fbxUrl(fbxRequest.getFbxUrl())
                .gifUrl(fbxRequest.getGifUrl())
                .title(fbxRequest.getTitle())
                .build();
    }
}
