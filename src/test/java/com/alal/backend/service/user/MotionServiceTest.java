package com.alal.backend.service.user;

import com.alal.backend.payload.response.ViewResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class MotionServiceTest {

    @Autowired
    private MotionService motionService;

    @DisplayName("sample, example을 받아 정상적으로 gif, fbx 주소를 반환하는지")
    @Test
    void findGifByMessagesTest(){
        // given
        List<String> messages = Arrays.asList("chunsic", "dance");
        Pageable pageable = PageRequest.of(0, 30);

        MotionService motionServiceMock = Mockito.mock(MotionService.class);

        List<ViewResponse> expectedResponses = new ArrayList<>();
        ViewResponse viewResponse = ViewResponse.builder().build();
        viewResponse.setGifUrls(Arrays.asList("sampleGif1", "sampleGif2"));
        viewResponse.setFbxUrls(Arrays.asList("exampleFbx1", "exampleFbx2"));

        expectedResponses.add(viewResponse);

        Page<ViewResponse> expectedPages = new PageImpl<>(expectedResponses, pageable, expectedResponses.size());

        Mockito.when(motionServiceMock.findGifByMessages(messages, pageable)).thenReturn(expectedPages);
        // when
        Page<ViewResponse> resultPages = motionServiceMock.findGifByMessages(messages, pageable);

        // then
        assertEquals(expectedPages.getContent(), resultPages.getContent());
    }
}