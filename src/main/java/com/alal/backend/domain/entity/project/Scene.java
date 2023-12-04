package com.alal.backend.domain.entity.project;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Scene {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Comment("장면 번호")
    private Long sceneId;

    @Column
    @Comment("장면 스토리")
    private String story;

    @Column
    @Comment("배경 위치")
    private String levelPosition;

    @Column
    @Comment("장면 썸네일")
    private String thumbnail;
}
