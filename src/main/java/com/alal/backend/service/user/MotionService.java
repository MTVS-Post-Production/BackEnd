package com.alal.backend.service.user;

import com.alal.backend.domain.dto.response.*;
import com.alal.backend.domain.entity.user.Motion;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.domain.entity.user.Voice;
import com.alal.backend.payload.request.user.FlaskRequest;
import com.alal.backend.payload.request.user.FlaskVoiceRequest;
import com.alal.backend.repository.user.MotionRepository;
import com.alal.backend.repository.user.UserRepository;
import com.alal.backend.repository.user.VoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

        return updateUserHistoryByResponseMessage(user, responseMessageToString);
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
        Voice voice = voiceRepository.findByUserId(userId);

        if (voice == null) {
            Voice createdVoice = Voice.fromDto(flaskResponse, userId, flaskRequest);
            voiceRepository.save(createdVoice);

            return VoiceResponse.fromEntity(createdVoice);
        }

        voice.updateVoiceUrl(flaskResponse, flaskRequest);

        return VoiceResponse.fromEntity(voice);
    }

    // Gif, Fbx Url을 찾는 공통 로직
    @Transactional(readOnly = true)
    public Page<ViewResponse> createViewResponse(List<String> userHistories, Pageable pageable) {
        List<Motion> motions = findMotionsByUserHistories(userHistories);
        List<GifUrlResponse> allGifs = getGifsFromMotions(motions);
        List<FbxUrlResponse> allFbxs = getFbxsFromMotions(motions);

        return createViewResponsePage(allGifs, allFbxs, pageable, userHistories);
    }

    private List<Motion> findMotionsByUserHistories(List<String> userHistories) {
        List<Motion> allMotions = new ArrayList<>();

        for (String userHistory : userHistories) {
            List<Motion> motions = motionRepository.findByMotionContaining(userHistory);
            allMotions.addAll(motions);
        }

        return allMotions;
    }

    private List<GifUrlResponse> getGifsFromMotions(List<Motion> motions) {
        return motions.stream()
                .map(motion -> GifUrlResponse.builder()
                        .gifFileName(getFileName(motion.getMotionGif()))
                        .gifUrl(motion.getMotionGif())
                        .build())
                .collect(Collectors.toList());
    }

    private List<FbxUrlResponse> getFbxsFromMotions(List<Motion> motions) {
        return motions.stream()
                .map(motion -> FbxUrlResponse.builder()
                        .fbxFileName(getFileName(motion.getMotionFbx()))
                        .fbxUrl(motion.getMotionFbx())
                        .build())
                .collect(Collectors.toList());
    }

    private String getFileName(String url) {
        String[] parts = url.split("/");
        String fileNameWithExtension = parts[parts.length - 1];
        int pos = fileNameWithExtension.lastIndexOf(".");
        return fileNameWithExtension.substring(0, pos);
    }

    private Page<ViewResponse> createViewResponsePage(List<GifUrlResponse> allGifs, List<FbxUrlResponse> allFbxs, Pageable pageable, List<String> userHistories) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allGifs.size());

        List<GifUrlResponse> pagedGifs = allGifs.subList(start, end);
        List<FbxUrlResponse> pagedFbxs = allFbxs.subList(start, end);

        List<ViewResponse> content = (start < end)
                ? Collections.singletonList(ViewResponse.fromList(pagedGifs, pagedFbxs, userHistories))
                : Collections.emptyList();

        return new PageImpl<>(content, pageable, allGifs.size());
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

    private Optional<List<String>> getUserUserHistory(User user) {
        return Optional.ofNullable(user.getUserHistory())
                .map(history -> Arrays.stream(history.split(", "))
                        .map(String::valueOf)
                        .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public Page<ViewResponse> createViewResponseByMotionName(String motionName, Pageable pageable, List<String> userHistories) {
        List<Motion> motions = findMotionsByUserHistory(motionName);
        List<GifUrlResponse> allGifs = getGifsFromMotions(motions);
        List<FbxUrlResponse> allFbxs = getFbxsFromMotions(motions);

        return createViewResponsePage(allGifs, allFbxs, pageable, userHistories);
    }

    private List<Motion> findMotionsByUserHistory(String motionName) {
        return motionRepository.findByMotionContaining(motionName);
    }

    public List<String> getUserHistories(Long userId) {
        User user = getUserById(userId);
        Optional<List<String>> userHistoriesOptional = getUserUserHistory(user);
        return userHistoriesOptional.orElse(Collections.emptyList());
    }
}
