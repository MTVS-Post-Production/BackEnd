package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateUserHistoryResponse {
    private Long userId;

    private String updateMessage;

    public static UpdateUserHistoryResponse fromEntity(User user) {
        return UpdateUserHistoryResponse.builder()
                .userId(user.getId())
                .updateMessage(user.getUserHistory())
                .build();
    }
}
