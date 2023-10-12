package com.alal.backend.controller.common;

import com.alal.backend.dto.request.RequestTestString;
import com.alal.backend.dto.response.ResponseTestString;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping
    public ResponseEntity<ResponseTestString> testGet(){
        ResponseTestString responseTestString = ResponseTestString.builder()
                .message("get successfully")
                .build();

        return ResponseEntity.ok(responseTestString);
    }

    @PostMapping
    public ResponseEntity<ResponseTestString> testPost(@RequestBody String message){
        ResponseTestString responseTestString = ResponseTestString.builder()
                .message(message)
                .build();

        return ResponseEntity.ok(responseTestString);
    }
}
