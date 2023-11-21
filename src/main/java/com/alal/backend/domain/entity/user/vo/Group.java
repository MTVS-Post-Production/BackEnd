package com.alal.backend.domain.entity.user.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Group {
    private String name;

    public static Group fromUser(String userGroup) {
        return Group.builder()
                .name(userGroup)
                .build();
    }

    @Override
    public String toString() {
        return name;
    }
}
