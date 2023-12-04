package com.alal.backend.domain.dto.request;

import com.alal.backend.domain.vo.StaffVO;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;
import java.util.List;

@Getter
@Setter
public class UploadProjectRequest {
    private String projectName;
    private String description;
    private List<StaffVO> staffs;
    private List<String> avatarName;
    private String poster;
    private String script;

    public byte[] encodeBase64() {
        return Base64.getDecoder().decode(script);
    }
}
