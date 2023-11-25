package com.alal.backend.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReadProjectsResponseList {
    private List<ReadProjectsResponse> readProjectsResponses;

    public static ReadProjectsResponseList from(List<ReadProjectsResponse> readProjectsResponses) {
        return ReadProjectsResponseList.builder()
                .readProjectsResponses(readProjectsResponses)
                .build();
    }
}
