package com.alal.backend.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alal.backend.domain.entity.user.User;
import com.alal.backend.lib.JsonUtils;
import com.alal.backend.payload.request.auth.FlaskRequest;
import com.alal.backend.payload.request.auth.SignUpRequest;
import com.alal.backend.payload.response.UpdateUserHistoryResponse;
import com.alal.backend.payload.response.ViewResponse;
import com.alal.backend.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;


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

//    @DisplayName("mp4 파일을 정상적으로 업로드하여 gif, fbx 주소를 가져오는지")
//    @Test
//    void findUrlByUploadMp4() {
//        // given
//        FlaskRequest flaskRequest = FlaskRequest.builder()
//                .fileName("SGVsbG8sIFdvcmxkIQ==")
//                .build();
//
//        Pageable pageable = PageRequest.of(0, 30);
//
//        List<ViewResponse> expectedResponses = new ArrayList<>();
//        ViewResponse viewResponse = ViewResponse.builder().build();
//        viewResponse.setGifUrls(Arrays.asList("sampleGif1", "sampleGif2"));
//        viewResponse.setFbxUrls(Arrays.asList("sampleFbx1", "sampleFbx2"));
//        expectedResponses.add(viewResponse);
//
//        Page<ViewResponse> expectedPages = new PageImpl<>(expectedResponses, pageable, expectedResponses.size());
//
//        Mockito.when(motionServiceMock.findUrlByUploadMp4(flaskRequest)).thenReturn(expectedPages);
//
//        // when
//        Page<ViewResponse> resultPages = motionServiceMock.findUrlByUploadMp4(flaskRequest);
//
//        // then
//        assertEquals(expectedPages.getContent(), resultPages.getContent());
//    }
//
//    @DisplayName("음성 파일을 정상적으로 업로드하여 클라이언트에 응답하는지")
//    @Test
//    void uploadAudioFileAndReceiveResponseTest() throws IOException {
//        // given
//        MockMultipartFile voiceFile = new MockMultipartFile(
//                "sample",
//                "sample.mp3",
//                "audio/mpeg",
//                "myFile".getBytes()
//        );
//
//        FlaskResponse expectedResponse = new FlaskResponse("Expected response message");
//
//        // Assume that the service is a mock or spy.
//        // You should replace 'motionService' with the actual name of your service instance.
//        Mockito.when(motionService.uploadAndRespondWithAudioFileSuccess(voiceFile)).thenReturn(expectedResponse);
//
//        // when
//        FlaskResponse actualResponse = motionService.uploadAndRespondWithAudioFileSuccess(voiceFile);
//
//        // then
//        Assertions.assertNotNull(actualResponse, "The response should not be null");
//        Assertions.assertNotEquals(expectedResponse.getResponseMessage(), actualResponse.getResponseMessage(),
//                "The response message should match the expected message");
//    }

}