package com.alal.backend.domain.entity.user;

import com.alal.backend.domain.dto.response.ImageFlaskResponse;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Comment("이미지 번호")
    private Long id;

    @Column
    @Comment("사용자 아이디")
    private Long userId;

    @Column
    @Comment("이미지 주소")
    private String albedoUrl;

    @Column
    @Comment("이미지 주소")
    private String meshMtlUrl;

    @Column
    @Comment("이미지 주소")
    private String meshObj;

    public static Image toEntity(ImageFlaskResponse flaskResponse, Long userId) {
        return Image.builder()
                .userId(userId)
                .albedoUrl(flaskResponse.getAlbedo())
                .meshMtlUrl(flaskResponse.getMesh_mtl())
                .meshObj(flaskResponse.getMesh_obj())
                .build();
    }

    public void updateImageUrl(ImageFlaskResponse flaskResponse) {
        this.albedoUrl = flaskResponse.getAlbedo();
        this.meshMtlUrl = flaskResponse.getMesh_mtl();
        this.meshObj = flaskResponse.getMesh_obj();
    }
}
