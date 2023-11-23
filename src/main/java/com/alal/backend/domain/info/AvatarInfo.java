package com.alal.backend.domain.info;

import com.alal.backend.domain.entity.project.Avatar;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AvatarInfo {
    private final String avatarName;
    private final String avatarImage;

    public static AvatarInfo fromEntity(Avatar avatar) {
        return AvatarInfo.builder()
                .avatarName(avatar.getAvatarName())
                .avatarImage(avatar.getAvatarImage())
                .build();
    }
}
