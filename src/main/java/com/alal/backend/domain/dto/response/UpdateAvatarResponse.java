package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.project.Avatar;
import com.alal.backend.domain.info.AvatarInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class UpdateAvatarResponse {
    private List<AvatarInfo> avatarInfos;

    public static UpdateAvatarResponse fromEntity(List<Avatar> avatars) {
        List<AvatarInfo> avatarInfoFromAvatars = fromAvatarList(avatars);

        return UpdateAvatarResponse.builder()
                .avatarInfos(avatarInfoFromAvatars)
                .build();
    }

    private static List<AvatarInfo> fromAvatarList(List<Avatar> avatars) {
        return avatars.stream()
                .map(AvatarInfo::fromEntity)
                .collect(Collectors.toList());
    }
}
