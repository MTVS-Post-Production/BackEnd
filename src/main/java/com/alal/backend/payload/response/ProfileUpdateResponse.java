package com.alal.backend.payload.response;

import com.alal.backend.domain.entity.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileUpdateResponse {
    private String name;

    private String userGroup;

    private String profileImage;

    public static ProfileUpdateResponse toEntity(User user) {
        return ProfileUpdateResponse.builder()
                .name(user.getName())
                .userGroup(user.getUserGroup())
                .profileImage(user.getImageUrl())
                .build();
    }
}
