package com.alal.backend.domain.entity.project;

import com.alal.backend.domain.info.AvatarInfo;
import com.alal.backend.domain.vo.Group;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Avatar {
    private static final String DEFAULT_IMAGE = "https://storage.googleapis.com/memo-log/defaultImage.jpg";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Comment("배역 번호")
    private Long avatarId;

    @Column
    @Comment("배역명")
    private String avatarName;

    @Column
    @Comment("배역 이미지")
    private String avatarImage;

    @Column
    @Comment("그룹명")
    private Group group;

    @OneToMany(mappedBy = "avatar", fetch = FetchType.LAZY)
    private List<ProjectAvatar> projectAvatars = new ArrayList<>();

    public static Avatar fromEntityAndName(Group group, String avatarName) {
        return Avatar.builder()
                .avatarName(avatarName)
                .avatarImage(DEFAULT_IMAGE)
                .group(group)
                .build();
    }

    public void update(String avatarUrl, AvatarInfo avatarInfo) {
        updateAvatarName(avatarInfo.getAvatarName());
        updateAvatarImage(avatarUrl);
    }

    private void updateAvatarName(String avatarName) {
        if (avatarName != null) {
            this.avatarName = avatarName;
        }
    }

    private void updateAvatarImage(String avatarUrl) {
        if (avatarImage != null) {
            this.avatarImage = avatarUrl;
        }
    }

    public void updateNameOnly(AvatarInfo avatarInfo) {
        updateAvatarName(avatarInfo.getAvatarName());
    }
}
