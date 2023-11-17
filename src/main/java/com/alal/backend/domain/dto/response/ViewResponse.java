package com.alal.backend.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class ViewResponse {

    private List<GifUrlResponse> gifUrls;

    private List<FbxUrlResponse> fbxUrls;

    public static ViewResponse fromList(List<GifUrlResponse> allGifs, List<FbxUrlResponse> allFbxs) {
        return ViewResponse.builder()
                .fbxUrls(allFbxs)
                .gifUrls(allGifs)
                .build();
    }
}
