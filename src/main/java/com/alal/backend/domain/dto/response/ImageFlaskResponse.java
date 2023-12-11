package com.alal.backend.domain.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageFlaskResponse {
    private String albedo;
    private String mesh_mtl;
    private String mesh_obj;
    private String url_mesh_example;
}
