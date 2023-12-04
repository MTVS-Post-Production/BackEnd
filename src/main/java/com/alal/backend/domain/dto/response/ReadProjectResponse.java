package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.project.*;
import com.alal.backend.domain.info.AvatarInfo;
import com.alal.backend.domain.info.StaffInfo;
import com.alal.backend.utils.Parser;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;
import java.io.UncheckedIOException;
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
    private final Long scriptId;

    public static ReadProjectResponse fromEntity(Project project, List<Avatar> avatars, List<Staff> staffs, Script script) throws IOException {
        List<AvatarInfo> avatarInfos = fromAvatarList(avatars);
        List<StaffInfo> staffInfos = fromStaffList(staffs);
        String base64Poster = fromPoster(project.getPoster());

        return ReadProjectResponse.builder()
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .poster(base64Poster)
                .avatarInfo(avatarInfos)
                .staffInfo(staffInfos)
                .scriptId(script.getScriptId())
                .build();
    }

    private static String fromPoster(String poster) throws IOException {
        return Parser.downloadAndEncodeImage(poster);
    }

    private static List<StaffInfo> fromStaffList(List<Staff> staffs) {
        return staffs.stream()
                .map(staff -> {
                    try {
                        return StaffInfo.fromEntity(staff);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private static List<AvatarInfo> fromAvatarList(List<Avatar> avatars) {
        return avatars.stream()
                .map(avatar -> {
                    try {
                        return AvatarInfo.fromEntity(avatar);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .collect(Collectors.toList());
    }

}
