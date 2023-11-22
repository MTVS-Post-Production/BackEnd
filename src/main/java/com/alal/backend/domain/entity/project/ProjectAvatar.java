package com.alal.backend.domain.entity.project;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectAvatar {
    @Id
    @GeneratedValue
    private Long projectAvatarId;

    @ManyToOne
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public static ProjectAvatar fromEntity(Avatar avatar, Project project) {
        return ProjectAvatar.builder()
                .avatar(avatar)
                .project(project)
                .build();
    }
}
