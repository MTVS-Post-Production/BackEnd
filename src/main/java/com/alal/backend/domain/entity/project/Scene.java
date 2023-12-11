package com.alal.backend.domain.entity.project;

import com.alal.backend.domain.dto.response.ReadSceneResponse;
import com.alal.backend.domain.dto.response.UploadSceneResponse;
import com.alal.backend.utils.Parser;
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
    @Comment("장면 아이디")
    private Long sceneId;

    @Column
    @Comment("장면 번호")
    private Long sceneNo;

    @Column
    @Comment("장면 스토리")
    private String story;

    @Column
    @Comment("배경 위치")
    private String levelPosition;

    @Column
    @Comment("장면 썸네일")
    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "script_id")
    private Script script;

    public ReadSceneResponse toReadSceneResponse() {
        return ReadSceneResponse.builder()
                .sceneNo(this.sceneNo)
                .story(this.story)
                .levelPosition(this.levelPosition)
                .thumbnail(encodeThumbnail())
                .build();
    }

    public UploadSceneResponse toUploadResponse() {
        return UploadSceneResponse.builder()
                .sceneNo(this.sceneNo)
                .build();
    }

    private String encodeThumbnail() {
        return Parser.downloadAndEncodeImage(this.thumbnail);
    }
}
