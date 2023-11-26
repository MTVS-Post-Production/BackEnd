package com.alal.backend.domain.info;

import com.alal.backend.domain.entity.project.Staff;
import com.alal.backend.domain.entity.project.StaffRole;
import com.alal.backend.utils.Parser;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;

@Getter
@Builder
public class StaffInfo {
    private final String staffName;
    private final String staffImage;
    private final StaffRole staffRole;

    public static StaffInfo fromEntity(Staff staff) throws IOException {
        String base64Image = Parser.downloadAndEncodeImage(staff.getStaffProfile());

        return StaffInfo.builder()
                .staffName(staff.getStaffName())
                .staffImage(base64Image)
                .staffRole(staff.getStaffRole())
                .build();
    }
}
