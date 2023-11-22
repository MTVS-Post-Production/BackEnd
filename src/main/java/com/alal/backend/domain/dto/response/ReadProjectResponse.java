package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.info.AvatarInfo;
import com.alal.backend.domain.info.StaffInfo;
import com.alal.backend.domain.entity.project.Avatar;
import com.alal.backend.domain.entity.project.Project;
import com.alal.backend.domain.entity.project.Staff;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReadProjectResponse {
    private final String projectName;
    private final String description;
    private final String poster;
    private final List<AvatarInfo> avatarInfo;
    private final List<StaffInfo> staffInfo;

    public static ReadProjectResponse fromEntity(Project project, List<Avatar> avatars, List<Staff> staffs) {
        List<AvatarInfo> avatarInfos = fromAvatarList(avatars);
        List<StaffInfo> staffInfos = fromStaffList(staffs);

        return ReadProjectResponse.builder()
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .poster(project.getPoster())
                .avatarInfo(avatarInfos)
                .staffInfo(staffInfos)
                .build();
    }

    private static List<StaffInfo> fromStaffList(List<Staff> staffs) {
        return staffs.stream()
                .map(StaffInfo::fromEntity)
                .collect(Collectors.toList());
    }

    private static List<AvatarInfo> fromAvatarList(List<Avatar> avatars) {
        return avatars.stream()
                .map(AvatarInfo::fromEntity)
                .collect(Collectors.toList());
    }
}
