package com.alal.backend.domain.dto.response;

import lombok.Builder;

@Builder
public class JwtTokenResponse {
    private String accessToken;

    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public static JwtTokenResponse fromCookie(String accessToken, String refreshToken) {
        return JwtTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
