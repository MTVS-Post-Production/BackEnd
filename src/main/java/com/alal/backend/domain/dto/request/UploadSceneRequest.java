package com.alal.backend.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
public class UploadSceneRequest {
    private Long scriptId;
    private String story;
    private String levelPosition;
    private String thumbNail;

    public byte[] decodeBase64() {
        return Base64.getDecoder().decode(thumbNail);
    }
}
