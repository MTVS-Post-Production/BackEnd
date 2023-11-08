package com.alal.backend.service.user;

import com.alal.backend.domain.entity.user.User;
import com.alal.backend.domain.entity.user.Voice;
import com.alal.backend.payload.request.user.FlaskRequest;
import com.alal.backend.payload.request.user.FlaskVoiceRequest;
import com.alal.backend.domain.dto.response.FlaskResponse;
import com.alal.backend.domain.dto.response.UpdateUserHistoryResponse;
import com.alal.backend.domain.dto.response.ViewResponse;
import com.alal.backend.domain.dto.response.VoiceResponse;
import com.alal.backend.repository.user.MotionRepository;
import com.alal.backend.repository.user.UserRepository;
import com.alal.backend.repository.user.VoiceRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MotionService {

    private final MotionRepository motionRepository;

    private final UserRepository userRepository;

    private final VoiceRepository voiceRepository;

    private final FlaskService flaskService;

    // 동영상 파일 Flask 서버와 통신 후 응답 메세지 유저 테이블에 저장
    @Transactional
    public UpdateUserHistoryResponse findUrlByUploadMp4(FlaskRequest flaskRequest, Long userId) {
        List<FlaskResponse> flaskResponses = flaskService.communicateWithFlaskServer(flaskRequest);

        String responseMessageToString = flaskResponses.stream()
                .map(FlaskResponse::getResponseMessage)
                .collect(Collectors.joining(", "));

        User user = getUserById(userId);
        UpdateUserHistoryResponse updateUserHistoryResponse = updateUserHistoryByResponseMessage(user, responseMessageToString);

        return updateUserHistoryResponse;
    }

    @Transactional
    public UpdateUserHistoryResponse updateUserHistoryByResponseMessage(User user, String responseMessageToString) {
        user.historyUpdate(responseMessageToString);

        return UpdateUserHistoryResponse.fromEntity(user);
    }

    // 음성 파일 Flask 서버와 통신 후 base64 문자열 응답
    @Transactional
    public VoiceResponse uploadAndRespondWithAudioFileSuccess(FlaskVoiceRequest flaskRequest, Long userId) {
        // Flask 서버 통신
        FlaskResponse flaskResponse = flaskService.communicateWithFlaskServerByVoice(flaskRequest, userId);
        Voice voice = getVoiceById(userId);

        if (voice == null) {
            Voice createdVoice = Voice.fromDto(flaskResponse, userId, flaskRequest);
            voiceRepository.save(createdVoice);

            VoiceResponse voiceResponse = VoiceResponse.fromEntity(createdVoice);

            return voiceResponse;
        }

        voice.updateVoiceUrl(flaskResponse, flaskRequest);
        VoiceResponse voiceResponse = VoiceResponse.fromEntity(voice);

        return voiceResponse;
    }

    // Gif, Fbx Url을 찾는 공통 로직
    public Page<ViewResponse> createViewResponse(Long userId, Pageable pageable) {
        User user = getUserById(userId);
        List<String> userHistories = getUserUserHistory(user);

        List<String> allGifs = new ArrayList<>();
        List<String> allFbxs = new ArrayList<>();

        for (String userHistory : userHistories) {
            Page<String> gifPage = motionRepository.findGifByMotionContaining(userHistory, pageable);
            Page<String> fbxPage = motionRepository.findFbxByMotionContaining(userHistory, pageable);

            allGifs.addAll(gifPage.getContent());
            allFbxs.addAll(fbxPage.getContent());
        }

        ViewResponse viewResponse = ViewResponse.fromList(allGifs, allFbxs);

        return new PageImpl<>(Collections.singletonList(viewResponse), pageable, allGifs.size());
    }

    @Transactional(readOnly = true)
    public VoiceResponse findByVoiceUrlWithUserId(Long userId, String modelName) {
        Voice voice = voiceRepository.findByUserIdAndModelName(userId, modelName);

        return VoiceResponse.fromEntity(voice);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
                );
    }

    private Voice getVoiceById(Long userId) {
        return voiceRepository.findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException("음성이 존재하지 않습니다.")
                );
    }

    private List<String> getUserUserHistory(User user
    ) {
        return Arrays.stream(user.getUserHistory().split(", "))
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

}
