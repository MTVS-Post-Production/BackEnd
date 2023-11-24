package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.project.*;
import com.alal.backend.domain.info.AvatarInfo;
import com.alal.backend.domain.info.StaffInfo;
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

    public static ReadProjectResponse fromEntities(Project project) {
        List<AvatarInfo> avatarInfos = extractAvatarInfos(project);
        List<StaffInfo> staffInfos = extractStaffInfos(project);

        return ReadProjectResponse.builder()
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .poster(project.getPoster())
                .avatarInfo(avatarInfos)
                .staffInfo(staffInfos)
                .build();
    }

    private static List<StaffInfo> extractStaffInfos(Project project) {
        return fromStaffList(
                project.getProjectStaffs().stream()
                        .map(ProjectStaff::getStaff)
                        .collect(Collectors.toList())
        );
    }

    private static List<AvatarInfo> extractAvatarInfos(Project project) {
        return fromAvatarList(
                project.getProjectAvatars().stream()
                        .map(ProjectAvatar::getAvatar)
                        .collect(Collectors.toList())
        );
    }
}
