package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.project.Script;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReadSceneResponseList {
    private List<ReadSceneResponse> readSceneResponses;
    private String scriptUrl;
    private Long scriptId;

    public static ReadSceneResponseList from(List<ReadSceneResponse> readSceneResponses, Script script) {
        return ReadSceneResponseList.builder()
                .readSceneResponses(readSceneResponses)
                .scriptUrl(script.getScriptUrl())
                .scriptId(script.getScriptId())
                .build();
    }
}
