package com.alal.backend.payload.request.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadMemoRequest {
    private MultipartFile csvFile;
}
