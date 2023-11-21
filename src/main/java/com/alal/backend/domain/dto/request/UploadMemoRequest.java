package com.alal.backend.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadMemoRequest {
    private MultipartFile csvFile;

    public String getExt() {
        return csvFile.getContentType();
    }
}
