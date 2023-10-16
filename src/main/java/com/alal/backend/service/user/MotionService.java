package com.alal.backend.service.user;

import com.alal.backend.payload.request.auth.FlaskRequest;
import com.alal.backend.payload.response.FlaskResponse;
import com.alal.backend.payload.response.ViewResponse;
import com.alal.backend.repository.user.MotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MotionService {

    private final MotionRepository motionRepository;

    private final WebClient webClient;

    @Value("${ai.model.serving.url}")
    private String flaskUrl;

    @Transactional(readOnly = true)
    public Page<ViewResponse> findUrlByUploadMp4(MultipartFile file, Pageable pageable) {
        // MultipartFile을 base64 인코딩, 파일 형식 추출
        FlaskRequest flaskRequest = convertMultipartFileToBase64(file);

        // Flask 서버 통신
        List<FlaskResponse> flaskResponses = communicateWithFlaskServer(flaskRequest);
        List<String> responseMessages = flaskResponses.stream()
                .map(FlaskResponse::getResponseMessage)
                .collect(Collectors.toList());
        List<ViewResponse> viewResponses = createViewResponse(responseMessages, pageable);

        return new PageImpl<>(viewResponses, pageable, viewResponses.size());
    }

    // MultipartFile을 base64로 변환하는 메서드
    private FlaskRequest convertMultipartFileToBase64(MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            String fileFormat = file.getContentType() != null ? file.getContentType().split("/")[1] : "";

            FlaskRequest flaskRequest = FlaskRequest.fromFile(Base64.getEncoder().encodeToString(fileBytes), fileFormat);
            return flaskRequest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Flask 서버 통신
    private List<FlaskResponse> communicateWithFlaskServer(FlaskRequest flaskRequest) {
        // 파일을 Flask 서버로 전송
        List<FlaskResponse> flaskResponses =  webClient.post()
                .uri(flaskUrl + "/checkpose/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("pose", flaskRequest.getBase64EncodedFile(),
                        "format", flaskRequest.getFileFormat()))
                .retrieve()
                .bodyToFlux(FlaskResponse.class)
                .collectList()
                .block();

        return flaskResponses;
    }

    // 메세지를 통한 gif, fbx 찾기
    @Transactional(readOnly = true)
    public Page<ViewResponse> findGifByMessages(List<String> messages, Pageable pageable) {
        // Flask 서버 통신
        // 분석한 결과를 문자열 리스트로 반환받음
        List<FlaskResponse> flaskResponses = WebClient.create()
                .post()
                .uri(flaskUrl + "/message")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Collections.singletonMap("pose", messages))
                .retrieve()
                .bodyToFlux(FlaskResponse.class)
                .collectList()
                .block();

        List<String> responseMessages = flaskResponses.stream()
                .map(FlaskResponse::getResponseMessage)
                .collect(Collectors.toList());

        // 공통 로직을 활용하여 gif, fbx 찾기
        List<ViewResponse> viewResponses = createViewResponse(responseMessages, pageable);

        return new PageImpl<>(viewResponses, pageable, viewResponses.size());
    }

    // Gif, Fbx Url을 찾는 공통 로직
    private List<ViewResponse> createViewResponse(List<String> messages, Pageable pageable) {
        List<ViewResponse> viewResponses = new ArrayList<>();

        for (String message : messages) {
            Page<String> gifPage = motionRepository.findGifByMotionContaining(message, pageable);
            Page<String> fbxPage = motionRepository.findFbxByMotionContaining(message, pageable);

            // 각 페이지의 URL을 가져와 ViewResponse 생성
            ViewResponse viewResponse = ViewResponse.fromPage(gifPage, fbxPage);
            viewResponses.add(viewResponse);
        }

        // List<ViewResponse>를 Page<ViewResponse>로 변환
        return viewResponses;
    }
}
