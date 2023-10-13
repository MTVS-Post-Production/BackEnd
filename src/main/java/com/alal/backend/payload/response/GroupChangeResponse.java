package com.alal.backend.payload.response;

import com.alal.backend.domain.entity.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupChangeResponse {
    private String name;

    private String userGroup;

    public static GroupChangeResponse toEntity(User user) {
        return GroupChangeResponse.builder()
                .name(user.getName())
                .userGroup(user.getUserGroup())
                .build();
    }
}
