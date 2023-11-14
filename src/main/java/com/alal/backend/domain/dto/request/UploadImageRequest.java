package com.alal.backend.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadImageRequest {
    private String imageEncodingString;
    private String description;
}
