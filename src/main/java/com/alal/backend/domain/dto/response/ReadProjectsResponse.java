package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.project.Avatar;
import com.alal.backend.domain.entity.project.Project;
import com.alal.backend.domain.entity.project.Staff;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReadProjectsResponse {
    private Long projectId;
    private String projectName;
    private String posterUrl;
    private String script;

    public static ReadProjectsResponse fromEntity(Project project) {
        return ReadProjectsResponse.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .posterUrl(project.getPoster())
                .script(project.getScript())
                .build();
    }
}
