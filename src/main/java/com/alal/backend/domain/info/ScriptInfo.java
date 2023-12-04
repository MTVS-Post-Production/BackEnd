package com.alal.backend.domain.info;

import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
public class ScriptInfo {
    private String script;

    public byte[] encodeBase64() {
        return Base64.getDecoder().decode(script);
    }
}
