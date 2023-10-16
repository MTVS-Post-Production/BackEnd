package com.alal.backend.domain.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Getter
@Table(name="motion")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Motion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Comment("모션 번호")
    private Long id;

    @Column
    @Comment("모션 제목 GIF")
    private String motionGif;

    @Column
    @Comment("모션 제목 FBX")
    private String motionFbx;
}
