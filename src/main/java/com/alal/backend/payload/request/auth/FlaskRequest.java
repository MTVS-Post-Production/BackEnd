package com.alal.backend.payload.request.auth;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class FlaskRequest {
    private String base64EncodedFile;

    private String fileFormat;

    public static FlaskRequest fromFile(String base64EncodedFile, String fileFormat) {
        return FlaskRequest.builder()
                .base64EncodedFile(base64EncodedFile)
                .fileFormat(fileFormat)
                .build();
    }
}
