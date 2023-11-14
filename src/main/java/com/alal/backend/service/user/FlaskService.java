package com.alal.backend.service.user;

import com.alal.backend.domain.dto.request.UploadImageRequest;
import com.alal.backend.domain.dto.response.FlaskResponse;
import com.alal.backend.domain.dto.response.ImageFlaskResponse;
import com.alal.backend.payload.request.user.FlaskRequest;
import com.alal.backend.payload.request.user.FlaskVoiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FlaskService {

    @Value("${ai.model.serving.url}")
    private String flaskUrl;

    @Value("${ai.model.serving.imageUrl}")
    private String flaskImageUrl;

    private final WebClient webClient;

    private static final String CHECK_POSE_URI = "/checkpose/";
    private static final String CONVERT_VOICE_URI = "/voice/convert";

    public List<FlaskResponse> communicateWithFlaskServer(FlaskRequest flaskRequest) {
        // 파일을 Flask 서버로 전송
        List<FlaskResponse> flaskResponses =  webClient.post()
                .uri(flaskUrl + CHECK_POSE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("pose", flaskRequest.getFileName()))
                .retrieve()
                .bodyToFlux(FlaskResponse.class)
                .collectList()
                .block();

        return flaskResponses;
    }

    // Flask 서버 통신 (음성)
    public FlaskResponse communicateWithFlaskServerByVoice(FlaskVoiceRequest flaskRequest, Long userId) {
        FlaskResponse flaskResponse =  webClient.post()
                .uri(flaskUrl + CONVERT_VOICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("voice", flaskRequest.getFileName(),
                        "model_name", flaskRequest.getModelName(),
                        "user_id", userId)
                )
                .retrieve()
                .bodyToMono(FlaskResponse.class)
                .block();

        return flaskResponse;
    }

    public ImageFlaskResponse uploadImage(UploadImageRequest uploadImageRequest, Long userId) {
        ImageFlaskResponse imageFlaskResponse = webClient.post()
                .uri(flaskImageUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("image", uploadImageRequest.getImageEncodingString(),
                        "image_name", uploadImageRequest.getDescription(),
                        "user_id", userId))
                .retrieve()
                .bodyToMono(ImageFlaskResponse.class)
                .block();

        return imageFlaskResponse;
    }
}
