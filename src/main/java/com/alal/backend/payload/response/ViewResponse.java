package com.alal.backend.payload.response;

import com.alal.backend.domain.entity.user.Motion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class ViewResponse {

    private List<String> gifUrls;

    private List<String> fbxUrls;

    public static ViewResponse fromPage(Page<String> gifPage, Page<String> fbxPage) {
        List<String> gifUrls = gifPage.getContent();
        List<String> fbxUrls = fbxPage.getContent();

        return ViewResponse.builder()
                .gifUrls(gifUrls)
                .fbxUrls(fbxUrls)
                .build();
    }
}
