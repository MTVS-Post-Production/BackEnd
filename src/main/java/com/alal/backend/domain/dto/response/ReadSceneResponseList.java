package com.alal.backend.domain.dto.response;

import com.alal.backend.domain.entity.project.Script;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReadSceneResponseList {
    private String scriptUrl;
}
