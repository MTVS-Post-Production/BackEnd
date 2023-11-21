package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.user.Memo;
import com.alal.backend.domain.entity.user.vo.Group;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadMemoResponse {
    private String uploadUrl;
    private String groupName;

    public static ReadMemoResponse fromEntity(Memo memo) {
        return ReadMemoResponse.builder()
                .uploadUrl(memo.getUploadUrl())
                .groupName(memo.getGroup().toString())
                .build();
    }
}
