package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.project.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadProjectsResponse {
    private String projectName;
    private String posterUrl;

    public static ReadProjectsResponse fromEntity(Project project) {
        return ReadProjectsResponse.builder()
                .projectName(project.getProjectName())
                .posterUrl(project.getPoster())
                .build();
    }
}
