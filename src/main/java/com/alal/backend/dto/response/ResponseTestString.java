package com.alal.backend.dto.response;

import com.alal.backend.dto.request.RequestTestString;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseTestString {
    private String message;

    public static ResponseTestString toRequest(RequestTestString requestTestString) {
        return ResponseTestString.builder()
                .message(requestTestString.getMessage())
                .build();
    }
}
