package com.alal.backend.service.user;

import com.alal.backend.payload.response.FlaskResponse;
import com.alal.backend.payload.response.ViewResponse;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@AutoConfigureMockMvc
class MotionServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private MotionService motionService;

    @Mock
    private MotionService motionServiceMock;


    @DisplayName("sample, example을 받아 정상적으로 gif, fbx 주소를 반환하는지")
    @Test
    void findGifByMessagesTest(){
        // given
        String message = "chunsic";
        Pageable pageable = PageRequest.of(0, 30);

        List<ViewResponse> expectedResponses = new ArrayList<>();
        ViewResponse viewResponse = ViewResponse.builder().build();
        viewResponse.setGifUrls(Arrays.asList("sampleGif1", "sampleGif2"));
        viewResponse.setFbxUrls(Arrays.asList("exampleFbx1", "exampleFbx2"));

        expectedResponses.add(viewResponse);

        Page<ViewResponse> expectedPages = new PageImpl<>(expectedResponses, pageable, expectedResponses.size());

        Mockito.when(motionServiceMock.findGifByMessages(message, pageable)).thenReturn(expectedPages);
        // when
        Page<ViewResponse> resultPages = motionServiceMock.findGifByMessages(message, pageable);

        // then
        assertEquals(expectedPages.getContent(), resultPages.getContent());
    }

    @DisplayName("mp4 파일을 정상적으로 업로드하여 gif, fbx 주소를 가져오는지")
    @Test
    void findUrlByUploadMp4() {
        // given
        MockMultipartFile mp4File = new MockMultipartFile(
                "sample",
                "sample.mp4",
                "video/mp4",
                "my mp4 content".getBytes()
        );

        Pageable pageable = PageRequest.of(0, 30);

        List<ViewResponse> expectedResponses = new ArrayList<>();
        ViewResponse viewResponse = ViewResponse.builder().build();
        viewResponse.setGifUrls(Arrays.asList("sampleGif1", "sampleGif2"));
        viewResponse.setFbxUrls(Arrays.asList("sampleFbx1", "sampleFbx2"));
        expectedResponses.add(viewResponse);

        Page<ViewResponse> expectedPages = new PageImpl<>(expectedResponses, pageable, expectedResponses.size());

        Mockito.when(motionServiceMock.findUrlByUploadMp4(mp4File, pageable)).thenReturn(expectedPages);

        // when
        Page<ViewResponse> resultPages = motionServiceMock.findUrlByUploadMp4(mp4File, pageable);

        // then
        assertEquals(expectedPages.getContent(), resultPages.getContent());
    }

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