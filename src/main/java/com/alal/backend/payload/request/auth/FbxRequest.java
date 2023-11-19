package com.alal.backend.payload.request.auth;

import lombok.Data;

@Data
public class FbxRequest {
    private String fbxUrl;
    private String gifUrl;
    private String title;
}
