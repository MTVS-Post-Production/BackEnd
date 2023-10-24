package com.alal.backend.payload.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProfileUpdateRequest {

    @Schema( type = "string", example = "미미고등학교", description="사용자 그룹을 입력해주세요")
    @NotBlank
    private String userGroup;

    @Schema( type = "string", example = "string", description="변경할 닉네임을 입력해주세요")
    @NotBlank
    private String userName;

    @Schema( type = "string", example = "string", description="변경할 닉네임을 입력해주세요")
    private String base64ProfileImage;
}
