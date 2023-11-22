package com.alal.backend.domain.entity.user;


import com.alal.backend.domain.dto.request.vo.StaffVO;
import com.alal.backend.domain.entity.user.vo.Group;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Comment("스태프 번호")
    private Long staffId;

    @Column
    @Comment("유저 아이디")
    private Long userId;

    @Column
    @Comment("역할")
    private StaffRole staffRole;

    @Column
    @Comment("그룹명")
    private Group group;

    @OneToMany(mappedBy = "staff")
    private List<ProjectMember> projectMembers = new ArrayList<>();

    public static Staff fromVOAndUser(User user, StaffVO staffVO) {
        return Staff.builder()
                .userId(user.getId())
                .group(new Group(user.getUserGroup()))
                .staffRole(staffVO.getStaffRole())
                .build();
    }
}
