package com.alal.backend.service.user;

import com.alal.backend.payload.response.FbxResponse;
import com.alal.backend.payload.response.ViewResponse;
import com.alal.backend.repository.user.MotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MotionService {

    private final MotionRepository motionRepository;

    // 메세지를 통한 gif, fbx 찾기
    @Transactional(readOnly = true)
    public Page<ViewResponse> findGifByMessages(List<String> messages, Pageable pageable) {
        List<ViewResponse> viewResponses = new ArrayList<>();

        for (String message : messages) {
            Page<String> gifPage = motionRepository.findGifByMotionContaining(message, pageable);
            Page<String> fbxPage = motionRepository.findFbxByMotionContaining(message, pageable);

            // 각 페이지의 URL을 가져와 ViewResponse 생성
            ViewResponse viewResponse = ViewResponse.fromPage(gifPage, fbxPage);
            viewResponses.add(viewResponse);
        }

        System.out.println("가져온거 = " + viewResponses);

        // List<ViewResponse>를 Page<ViewResponse>로 변환
        return new PageImpl<>(viewResponses, pageable, viewResponses.size());
    }
}
