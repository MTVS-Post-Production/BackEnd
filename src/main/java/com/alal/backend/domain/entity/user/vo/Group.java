package com.alal.backend.domain.entity.user.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {
    private String name;

    public Group(String name) {
        this.name = name;
    }
}
