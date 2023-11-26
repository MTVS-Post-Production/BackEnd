package com.alal.backend.domain.entity.project;

import com.alal.backend.domain.dto.request.UploadProjectRequest;
import com.alal.backend.domain.vo.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Comment("프로젝트 번호")
    private Long projectId;

    @Column
    @Comment("프로젝트명")
    private String projectName;

    @Column
    @Comment("그룹명")
    private Group group;

    @Column
    @Comment("설명")
    private String description;

    @Column
    @Comment("포스터")
    private String poster;

    @Column
    @Comment("클라이언트 대본 주소")
    private String script;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<ProjectStaff> projectStaffs = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    private List<ProjectAvatar> projectAvatars = new ArrayList<>();

    public static Project fromDto(Group group, UploadProjectRequest uploadProjectRequest, String posterUrl) {
        return Project.builder()
                .group(group)
                .projectName(uploadProjectRequest.getProjectName())
                .description(uploadProjectRequest.getDescription())
                .poster(posterUrl)
                .script(uploadProjectRequest.getScript())
                .build();
    }
}
