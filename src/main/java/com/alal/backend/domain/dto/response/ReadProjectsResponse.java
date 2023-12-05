package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.project.Project;
import com.alal.backend.utils.Parser;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;

@Getter
@Builder
public class ReadProjectsResponse {
    private Long projectId;
    private String projectName;
    private String posterUrl;

    public static ReadProjectsResponse fromEntity(Project project) throws IOException {
        String poster = Parser.downloadAndEncodeImage(project.getPoster());

        return ReadProjectsResponse.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .posterUrl(poster)
                .build();
    }
}
