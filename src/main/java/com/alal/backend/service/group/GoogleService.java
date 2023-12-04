package com.alal.backend.service.group;

import com.alal.backend.domain.dto.request.UploadProjectRequest;
import com.alal.backend.domain.dto.request.UploadSceneRequest;
import com.alal.backend.utils.Parser;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleService {
    private static final String SCENE_FOLDER = "Scene/";
    private static final String SCRIPTS_FOLDER = "Scripts/";
    private static final String POSTER_FOLDER = "Poster/";

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

    public String uploadScripts(UploadProjectRequest uploadProjectRequest) {
        byte[] decodedBytes = uploadProjectRequest.encodeBase64();
        String scriptName = SCRIPTS_FOLDER + uploadProjectRequest.getProjectName();

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, scriptName)
                        .setContentType("text/csv")
                        .build(),
                decodedBytes
        );

        return parser.parseBlobInfo(blobInfo);
    }


    public String uploadPoster(UploadProjectRequest uploadProjectRequest) {
        byte[] decodedBytes = Base64.getDecoder().decode(uploadProjectRequest.getPoster());
        String posterUrl = POSTER_FOLDER + uploadProjectRequest.getProjectName();

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, posterUrl)
                        .setContentType("image/png")
                        .build(),
                decodedBytes
        );

        return parser.parseBlobInfo(blobInfo);
    }
}
