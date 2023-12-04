package com.alal.backend.domain.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public class ReadSceneResponseList {
    private List<ReadSceneResponse> readSceneResponses;

    public static ReadSceneResponseList from(List<ReadSceneResponse> readSceneResponses) {
        return ReadSceneResponseList.builder()
                .readSceneResponses(readSceneResponses)
                .build();
    }
}
