package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.user.Project;
import com.alal.backend.domain.entity.user.vo.Group;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UploadProjectResponse {
    private String projectName;
    private String description;
    private String posterName;
    private Group group;

    public static UploadProjectResponse fromEntity(Project project) {
        return UploadProjectResponse.builder()
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .posterName(project.getProjectName())
                .group(project.getGroup())
                .build();
    }
}
