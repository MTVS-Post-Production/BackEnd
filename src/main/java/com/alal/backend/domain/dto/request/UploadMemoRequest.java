package com.alal.backend.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadMemoRequest {
    private String csvFile;
}
