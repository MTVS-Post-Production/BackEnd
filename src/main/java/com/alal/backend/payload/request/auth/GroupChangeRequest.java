package com.alal.backend.payload.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GroupChangeRequest {

    @Schema( type = "string", example = "미미고등학교", description="사용자 그룹을 입력해주세요")
    @NotBlank
    @NotNull
    private String userGroup;
}
