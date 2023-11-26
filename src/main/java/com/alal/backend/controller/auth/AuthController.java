package com.alal.backend.controller.auth;



import com.alal.backend.advice.payload.ErrorResponse;
import com.alal.backend.config.security.token.CurrentUser;
import com.alal.backend.config.security.token.UserPrincipal;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.payload.request.auth.*;
import com.alal.backend.payload.request.user.ProfileUpdateRequest;
import com.alal.backend.payload.response.AuthResponse;
import com.alal.backend.payload.response.Message;
import com.alal.backend.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Authorization", description = "Authorization API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "유저 정보 확인", description = "현제 접속된 유저정보를 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 확인 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class) ) } ),
        @ApiResponse(responseCode = "400", description = "유저 확인 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value = "/")
    public ResponseEntity<?> whoAmI(
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return authService.whoAmI(userPrincipal);
    }

    @Operation(summary = "유저 정보 삭제", description = "현제 접속된 유저정보를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 삭제 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "유저 삭제 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @DeleteMapping(value = "/")
    public ResponseEntity<?> delete(
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ){
        return authService.delete(userPrincipal);
    }

    @Operation(summary = "유저 정보 갱신", description = "현제 접속된 유저의 프로필을 새로 지정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 갱신 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "유저 정보 갱신 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PatchMapping(value = "/")
    public ResponseEntity<?> modify(
//            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 ChangePasswordRequest를 참고해주세요.", required = true) @RequestBody ProfileUpdateRequest profileUpdateRequest
            ) {
        Long userId = 1L;
//        Long userId = userPrincipal.getId();
        return authService.modify(userId, profileUpdateRequest);
    }

    @Operation(summary = "유저 로그인", description = "유저 로그인을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 로그인 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class) ) } ),
        @ApiResponse(responseCode = "400", description = "유저 로그인 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value = "/signin")
    public ResponseEntity<?> signin(
        @Parameter(description = "Schemas의 SignInRequest를 참고해주세요.", required = true) @Valid @RequestBody SignInRequest signInRequest
    ) {
        return authService.signin(signInRequest);
    }

    @Operation(summary = "유저 회원가입", description = "유저 회원가입을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "회원가입 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(
        @Parameter(description = "Schemas의 SignUpRequest를 참고해주세요.", required = true) @Valid @RequestBody SignUpRequest signUpRequest
    ) {
        return authService.signup(signUpRequest);
    }

    @Operation(summary = "토큰 갱신", description = "신규 토큰 갱신을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토큰 갱신 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class) ) } ),
        @ApiResponse(responseCode = "400", description = "토큰 갱신 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refresh(
        @Parameter(description = "Schemas의 RefreshTokenRequest를 참고해주세요.", required = true) @Valid @RequestBody RefreshTokenRequest tokenRefreshRequest
    ) {
        return authService.refresh(tokenRefreshRequest);
    }


    @Operation(summary = "유저 로그아웃", description = "유저 로그아웃을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value="/signout")
    public ResponseEntity<?> signout(
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal, 
        @Parameter(description = "Schemas의 RefreshTokenRequest를 참고해주세요.", required = true) @Valid @RequestBody RefreshTokenRequest tokenRefreshRequest
    ) {
        return authService.signout(tokenRefreshRequest);
    }
}
