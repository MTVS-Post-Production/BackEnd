package com.alal.backend.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alal.backend.domain.entity.user.User;
import com.alal.backend.domain.dto.response.UpdateUserHistoryResponse;
import com.alal.backend.domain.dto.response.ViewResponse;
import com.alal.backend.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class MotionServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private MotionService motionService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private MotionService motionServiceMock;


    @DisplayName("sample, example을 받아 정상적으로 userHistory에 저장 or 수정하는지")
    @Test
    void findGifByMessagesTest(){
        // given
        String message = "chunsic";
        User user = User.builder()
                .id(1L)
                .build();

        // when
        UpdateUserHistoryResponse updateUserHistoryResponse = motionService.updateUserHistoryByResponseMessage(user, message);

        // then
        assertTrue(message.equals(updateUserHistoryResponse.getUpdateMessage()) &&
                user.getId().equals(updateUserHistoryResponse.getUserId()));
    }

    @DisplayName("유저 아이디를 토대로 gif, fbx 주소를 가져오는지")
    @Test
    void findUrlByUploadMp4() {
        // given
        User user = User.builder()
                .id(1L)
                .userHistory("sample")
                .build();

        Pageable pageable = PageRequest.of(0, 30);

        List<ViewResponse> expectedResponses = new ArrayList<>();
        ViewResponse viewResponse = ViewResponse.builder().build();
        viewResponse.setGifUrls(Arrays.asList("sampleGif1", "sampleGif2"));
        viewResponse.setFbxUrls(Arrays.asList("sampleFbx1", "sampleFbx2"));
        expectedResponses.add(viewResponse);

        Page<ViewResponse> expectedPages = new PageImpl<>(expectedResponses, pageable, expectedResponses.size());

        Mockito.when(motionServiceMock.createViewResponse(user.getId(), pageable)).thenReturn(expectedPages);

        // when
        Page<ViewResponse> resultPages = motionServiceMock.createViewResponse(user.getId(), pageable);

        // then
        assertEquals(expectedPages.getContent(), resultPages.getContent());
    }
}