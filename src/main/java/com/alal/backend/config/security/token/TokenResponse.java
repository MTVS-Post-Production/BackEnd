package com.alal.backend.config.security.token;

import com.alal.backend.domain.mapping.TokenMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;

    public static TokenResponse toTokenMapping(TokenMapping tokenMapping) {
        return TokenResponse.builder()
                .accessToken(tokenMapping.getAccessToken())
                .refreshToken(tokenMapping.getRefreshToken())
                .tokenType("Bearer")
                .build();
    }
}
