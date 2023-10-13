package com.alal.backend.dto.response;

import com.alal.backend.domain.entity.user.Token;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseTestToken {
    private String accessToken;

    private String refreshToken;

    public static ResponseTestToken toEntity(Token token) {
        return ResponseTestToken.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}
