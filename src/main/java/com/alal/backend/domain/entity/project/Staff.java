package com.alal.backend.domain.entity.project;


import com.alal.backend.domain.vo.StaffVO;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.domain.vo.Group;
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
    @Comment("스태프 이름")
    private String staffName;

    @Column
    @Comment("스태프 프로필")
    private String staffProfile;

    @Column
    @Comment("역할")
    private StaffRole staffRole;

    @Column
    @Comment("그룹명")
    private Group group;

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    private List<ProjectStaff> projectStaffs = new ArrayList<>();

    public static Staff fromVOAndUser(User user, StaffVO staffVO) {
        return Staff.builder()
                .userId(user.getId())
                .staffName(user.getName())
                .staffProfile(user.getImageUrl())
                .group(new Group(user.getUserGroup()))
                .staffRole(staffVO.getStaffRole())
                .build();
    }
}
