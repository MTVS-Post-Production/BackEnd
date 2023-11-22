package com.alal.backend.domain.entity.project;

import com.alal.backend.domain.entity.user.vo.Group;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Comment("메모 번호")
    private Long memoId;

    @Column
    @Comment("메모 구글 스토리지 주소")
    private String uploadUrl;

    @Column
    @Comment("그룹명")
    private Group group;

    public static Memo fromEntity(String uploadUrl, String userGroup) {
        return Memo.builder()
                .uploadUrl(uploadUrl)
                .group(new Group(userGroup))
                .build();
    }
}
