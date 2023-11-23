package com.alal.backend.domain.dto.request;

import com.alal.backend.domain.info.AvatarInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateAvatarRequest {
    private List<AvatarInfo> avatarInfos;
}
