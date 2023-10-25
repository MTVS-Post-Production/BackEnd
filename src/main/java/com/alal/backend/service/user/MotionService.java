package com.alal.backend.service.user;

import com.alal.backend.domain.entity.user.User;
import com.alal.backend.domain.entity.user.Voice;
import com.alal.backend.payload.request.auth.FlaskVoiceRequest;
import com.alal.backend.payload.request.auth.FlaskRequest;
import com.alal.backend.payload.response.FlaskResponse;
import com.alal.backend.payload.response.UpdateUserHistoryResponse;
import com.alal.backend.payload.response.ViewResponse;
import com.alal.backend.payload.response.VoiceResponse;
import com.alal.backend.repository.user.MotionRepository;
import com.alal.backend.repository.user.UserRepository;
import com.alal.backend.repository.user.VoiceRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class MotionService {

    private final MotionRepository motionRepository;

    private final UserRepository userRepository;

    private final VoiceRepository voiceRepository;

    private final WebClient webClient;

    @Value("${ai.model.serving.url}")
    private String flaskUrl;

    // 동영상 파일 Flask 서버와 통신 후 응답 메세지 유저 테이블에 저장
    @Transactional
    public UpdateUserHistoryResponse findUrlByUploadMp4(FlaskRequest flaskRequest, Long userId) {
        // Flask 서버 통신
        List<FlaskResponse> flaskResponses = communicateWithFlaskServer(flaskRequest);

        // 가져온 문자열 리스트를 문자열로 변환
        String responseMessageToString = flaskResponses.stream()
                .map(FlaskResponse::getResponseMessage)
                .collect(Collectors.joining(", "));

        Optional<User> userRepositoryById = userRepository.findById(userId);
        User user = userRepositoryById.get();

        UpdateUserHistoryResponse updateUserHistoryResponse = updateUserHistoryByResponseMessage(user, responseMessageToString);

        return updateUserHistoryResponse;
    }

    @Transactional
    public UpdateUserHistoryResponse updateUserHistoryByResponseMessage(User user, String responseMessageToString) {
        if (user.getUserHistory() == null) {
            user.historyUpdate(responseMessageToString);
        }
        else {
            user.historyUpdate(responseMessageToString);
        }

        UpdateUserHistoryResponse updateUserHistoryResponse = UpdateUserHistoryResponse.fromEntity(user);
        return updateUserHistoryResponse;
    }

    // 음성 파일 Flask 서버와 통신 후 base64 문자열 응답
    @Transactional
    public VoiceResponse uploadAndRespondWithAudioFileSuccess(FlaskVoiceRequest flaskRequest, Long userId) {
        // Flask 서버 통신
        FlaskResponse flaskResponse = communicateWithFlaskServerByVoice(flaskRequest, userId);
        Optional<Voice> voiceOptional = voiceRepository.findById(userId);

        if (!voiceOptional.isPresent()) {
            Voice createdVoice = Voice.fromDto(flaskResponse, userId, flaskRequest);
            voiceRepository.save(createdVoice);

            VoiceResponse voiceResponse = VoiceResponse.fromEntity(createdVoice);

            return voiceResponse;
        }
        Voice voice = voiceOptional.get();

        voice.updateVoiceUrl(flaskResponse, flaskRequest);
        VoiceResponse voiceResponse = VoiceResponse.fromEntity(voice);

        return voiceResponse;
    }


    // Flask 서버 통신 (동영상 파일)
    private List<FlaskResponse> communicateWithFlaskServer(FlaskRequest flaskRequest) {
        // 파일을 Flask 서버로 전송
        List<FlaskResponse> flaskResponses =  webClient.post()
                .uri(flaskUrl + "/checkpose/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("pose", flaskRequest.getFileName()))
                .retrieve()
                .bodyToFlux(FlaskResponse.class)
                .collectList()
                .block();

        return flaskResponses;
    }

    // Flask 서버 통신 (음성)
//    private FlaskResponse communicateWithFlaskServerByVoice(FlaskVoiceRequest flaskRequest) {
//        FlaskResponse flaskResponse =  webClient.post()
//                .uri(flaskUrl + "/voice/convert")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(Map.of("voice", flaskRequest.getFileName(),
//                        "model_name", flaskRequest.getModelName()
//                        ))
//                .retrieve()
//                .bodyToMono(FlaskResponse.class)
//                .block();
//
//        return flaskResponse;
//    }

    // 메모리 억지로 늘리기
    private FlaskResponse communicateWithFlaskServerByVoice(FlaskVoiceRequest flaskRequest, Long userId) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 10))
                .build();

        WebClient localWebClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .build();

        FlaskResponse flaskResponse = localWebClient.post()
                .uri(flaskUrl + "/voice/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "voice", flaskRequest.getFileName(),
                        "model_name", flaskRequest.getModelName(),
                        "user_id", userId))
                .retrieve()
                .bodyToMono(FlaskResponse.class)
                .block();

        return flaskResponse;
    }

    // Gif, Fbx Url을 찾는 공통 로직
    public Page<ViewResponse> createViewResponse(Long userId, Pageable pageable) {
        List<ViewResponse> viewResponses = new ArrayList<>();
        List<String> allGifs = new ArrayList<>();
        List<String> allFbxs = new ArrayList<>();

        Optional<User> userRepositoryById = userRepository.findById(userId);

        User user = userRepositoryById.get();

        List<String> userHistories = Arrays.stream(user.getUserHistory().split(", "))
                .map(String::valueOf)
                .collect(Collectors.toList());

        for (String userHistory : userHistories) {
            Page<String> gifPage = motionRepository.findGifByMotionContaining(userHistory, pageable);
            Page<String> fbxPage = motionRepository.findFbxByMotionContaining(userHistory, pageable);

            // 각 페이지의 URL을 가져와 리스트에 추가
            allGifs.addAll(gifPage.getContent());
            allFbxs.addAll(fbxPage.getContent());
        }

        // 전체 아이템 수 계산
        int totalItems = allGifs.size();

        // 전체 아이템을 하나의 ViewResponse로 만들어서 리스트에 추가
        ViewResponse viewResponse = ViewResponse.fromList(allGifs, allFbxs);
        viewResponses.add(viewResponse);

        return new PageImpl<>(viewResponses, pageable, totalItems);
    }

    @Transactional(readOnly = true)
    public VoiceResponse findByVoiceUrlWithUserId(Long userId, String modelName) {
        Voice voice = voiceRepository.findByUserIdAndModelName(userId, modelName);

        VoiceResponse voiceResponse = VoiceResponse.fromEntity(voice);

        return voiceResponse;
    }
}
