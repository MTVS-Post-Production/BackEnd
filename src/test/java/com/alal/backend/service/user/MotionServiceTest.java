package com.alal.backend.service.user;

import com.alal.backend.payload.response.ViewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class MotionServiceTest {

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
        expectedResponses.add(viewResponse);

        Page<ViewResponse> expectedPages = new PageImpl<>(expectedResponses, pageable, expectedResponses.size());

        Mockito.when(motionServiceMock.findUrlByUploadMp4(mp4File,pageable)).thenReturn(expectedPages);

        // when
        Page<ViewResponse> resultPages = motionServiceMock.findUrlByUploadMp4(mp4File, pageable);

        // then
        assertEquals(expectedPages.getContent(), resultPages.getContent());
    }
}