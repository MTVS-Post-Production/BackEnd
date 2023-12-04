package com.alal.backend.service.group;

import com.alal.backend.domain.dto.request.UploadSceneRequest;
import com.alal.backend.utils.Parser;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleService {
    private static final String SCENE_FOLDER = "Scene/";

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private final Parser parser;
    private final Storage storage;

    public String uploadImage(UploadSceneRequest uploadSceneRequest) {
        byte[] decodedBytes = uploadSceneRequest.decodeBase64();
        String posterUrl = SCENE_FOLDER + UUID.randomUUID();

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, posterUrl)
                        .setContentType("image/png")
                        .build(),
                decodedBytes
        );

        return parser.parseBlobInfo(blobInfo);
    }
}
