package com.alal.backend.payload.response;

import lombok.*;

import java.util.List;
import org.springframework.data.domain.Page;

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

    public static ViewResponse fromList(List<String> allGifs, List<String> allFbxs) {
        return ViewResponse.builder()
                .fbxUrls(allFbxs)
                .gifUrls(allGifs)
                .build();
    }
}
