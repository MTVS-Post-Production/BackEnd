package com.alal.backend.domain.entity.project;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectMember {
    @Id
    @GeneratedValue
    private Long projectMemberId;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public static ProjectMember fromEntity(Staff staff, Project project) {
        return ProjectMember.builder()
                .staff(staff)
                .project(project)
                .build();
    }
}
