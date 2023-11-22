package com.alal.backend.domain.entity.project;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StaffRole {
    DIRECTER("directer"),
    GENERAL("general");

    private final String role;

    StaffRole(String role) {
        this.role = role;
    }

    @JsonCreator
    public static StaffRole fromString(String value) {
        for (StaffRole role : StaffRole.values()) {
            if (role.role.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("유요하지 않는 역할입니다.");
    }

}
