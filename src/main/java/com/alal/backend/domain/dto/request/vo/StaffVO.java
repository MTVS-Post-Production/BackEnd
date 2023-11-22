package com.alal.backend.domain.dto.request.vo;

import com.alal.backend.domain.entity.project.StaffRole;
import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class StaffVO {
    private String email;
    private StaffRole staffRole;
}
