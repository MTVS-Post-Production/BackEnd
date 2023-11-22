package com.alal.backend.domain.info;

import com.alal.backend.domain.entity.project.Staff;
import com.alal.backend.domain.entity.project.StaffRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StaffInfo {
    private final String staffName;
    private final String staffImage;
    private final StaffRole staffRole;

    public static StaffInfo fromEntity(Staff staff) {
        return StaffInfo.builder()
                .staffName(staff.getStaffName())
                .staffImage(staff.getStaffProfile())
                .staffRole(staff.getStaffRole())
                .build();
    }
}
