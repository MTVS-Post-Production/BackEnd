package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.user.Memo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UploadMemoResponse {
    private static final String SuccessMessage = "성공적으로 생성되었습니다.";

    private Integer memoSize;
    private String message;

    public static UploadMemoResponse fromEntity(List<Memo> memos) {
        return UploadMemoResponse.builder()
                .memoSize(memos.size())
                .message(SuccessMessage)
                .build();
    }
}
