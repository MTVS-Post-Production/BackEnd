package com.alal.backend.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GifUrlResponse {
    private String gifFileName;
    private String gifUrl;
}
