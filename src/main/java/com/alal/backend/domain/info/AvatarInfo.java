package com.alal.backend.domain.info;

import com.alal.backend.domain.entity.project.Avatar;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;

import static com.alal.backend.utils.Parser.downloadAndEncodeImage;

@Getter
@Builder
public class AvatarInfo {
    private final Long avatarId;
    private final String avatarName;
    private final String avatarImage;

    public static AvatarInfo fromEntity(Avatar avatar) throws IOException {
        String base64Image = downloadAndEncodeImage(avatar.getAvatarImage());

        return AvatarInfo.builder()
                .avatarId(avatar.getAvatarId())
                .avatarName(avatar.getAvatarName())
                .avatarImage(base64Image)
                .build();
    }
}
