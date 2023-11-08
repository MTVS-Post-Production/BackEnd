package com.alal.backend.domain.entity.user;

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
    private Long id;

    @Column
    @Comment("작성자")
    private String author;

    @Column
    @Comment("메모 내용")
    private String content;

    @Column
    @Comment("작성 일시")
    private String writtenAt;

    @Column
    @Comment("그룹명")
    private Group group;

    public static Memo toEntity(String author, String content, String writtenAt, String groupName) {
        return Memo.builder()
                .author(author)
                .content(content)
                .writtenAt(writtenAt)
                .group(new Group(groupName))
                .build();
    }
}
