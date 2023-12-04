package com.alal.backend.domain.entity.project;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Script {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Comment("대본 번호")
    private Long scriptId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @Comment("프로젝트 번호")
    private Project project;

    @Column
    @Comment("업로드된 대본 Url")
    private String scriptUrl;
}
