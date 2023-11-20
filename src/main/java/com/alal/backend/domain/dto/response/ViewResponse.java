package com.alal.backend.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class ViewResponse {

    private final List<GifUrlResponse> gifUrls;
    private final List<FbxUrlResponse> fbxUrls;
    private final List<String> resultMotions;
    private final String motionName;

    public static ViewResponse fromList(List<GifUrlResponse> allGifs, List<FbxUrlResponse> allFbxs, List<String> userHistories) {
        return ViewResponse.builder()
                .gifUrls(allGifs)
                .fbxUrls(allFbxs)
                .resultMotions(userHistories)
                .build();
    }
}
