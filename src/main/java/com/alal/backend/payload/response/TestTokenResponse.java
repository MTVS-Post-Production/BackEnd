package com.alal.backend.payload.response;

import com.alal.backend.domain.entity.user.Token;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TestTokenResponse {
    private String accessToken;

    private String refreshToken;

    public static TestTokenResponse toEntity(Token token) {
        return TestTokenResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}
