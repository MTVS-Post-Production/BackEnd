package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.user.Memo;
import com.alal.backend.domain.entity.user.vo.Group;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadMemoResponse {
    private Long id;
    private String author;
    private String content;
    private String writtenAt;
    private Group group;

    public static ReadMemoResponse fromEntity(Memo memo) {
        return ReadMemoResponse.builder()
                .id(memo.getId())
                .author(memo.getAuthor())
                .content(memo.getContent())
                .writtenAt(memo.getWrittenAt())
                .group(memo.getGroup())
                .build();
    }
}
