package com.alal.backend.service.user;

import com.alal.backend.domain.dto.request.UpdateAvatarRequest;
import com.alal.backend.domain.dto.response.UpdateAvatarResponse;
import com.alal.backend.domain.entity.project.Avatar;
import com.alal.backend.domain.info.AvatarInfo;
import com.alal.backend.repository.user.AvatarRepository;
import com.alal.backend.utils.Parser;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final Parser parser;
    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Transactional
    public UpdateAvatarResponse updateAvatar(UpdateAvatarRequest avatarRequest) {
        List<Avatar> avatars = findAvatars(avatarRequest);
        List<String> avatarUrls = uploadAvatarsAndGetUrls(avatarRequest.getAvatarInfos());

        avatars = updateAvatars(avatars, avatarUrls, avatarRequest.getAvatarInfos());

        return UpdateAvatarResponse.fromEntity(avatars);
    }

    private List<Avatar> findAvatars(UpdateAvatarRequest avatarRequest) {
        List<Long> avatarIds = avatarRequest.getAvatarInfos().stream()
                .map(AvatarInfo::getAvatarId)
                .collect(Collectors.toList());

        return avatarRepository.findAllById(avatarIds);
    }

    private List<String> uploadAvatarsAndGetUrls(List<AvatarInfo> avatarInfos) {
        return avatarInfos.stream()
                .map(this::uploadAvatarAndGetUrl)
                .collect(Collectors.toList());
    }

    private String uploadAvatarAndGetUrl(AvatarInfo avatarInfo) {
        if (avatarInfo.getAvatarImage() == null || avatarInfo.getAvatarImage().isEmpty()) {
            return null;
        }
        byte[] decodedBytes = Base64.getDecoder().decode(avatarInfo.getAvatarImage());

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, String.valueOf(avatarInfo.getAvatarId()))
                        .setContentType("image/png")
                        .build(),
                decodedBytes
        );

        return parser.parseBlobInfo(blobInfo);
    }

    private List<Avatar> updateAvatars(List<Avatar> avatars, List<String> avatarUrls, List<AvatarInfo> avatarInfos) {
        IntStream.range(0, avatars.size())
                .forEach(i -> updateAvatar(avatars.get(i), avatarUrls.get(i), avatarInfos.get(i)));
        return avatarRepository.saveAll(avatars);
    }

    private void updateAvatar(Avatar avatar, String avatarUrl, AvatarInfo avatarInfo) {
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            avatar.updateNameOnly(avatarInfo);
            return;
        }
        avatar.update(avatarUrl, avatarInfo);
    }
}
