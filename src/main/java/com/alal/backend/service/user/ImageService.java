package com.alal.backend.service.user;

import com.alal.backend.domain.dto.request.UploadImageRequest;
import com.alal.backend.domain.dto.response.ImageFlaskResponse;
import com.alal.backend.domain.dto.response.ReadImageResponse;
import com.alal.backend.domain.dto.response.UploadImageResponse;
import com.alal.backend.domain.entity.user.Image;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.repository.user.ImageRepository;
import com.alal.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final FlaskService flaskService;

    @Transactional
    public UploadImageResponse uploadImage(UploadImageRequest uploadImageRequest, Long userId) {
        User user = getUser(userId);
        ImageFlaskResponse imageFlaskResponse = flaskService.uploadImage(uploadImageRequest, userId);

        Image image = imageRepository.findByUserId(user.getId());
        if (image == null) {
            Image createdImage = Image.toEntity(imageFlaskResponse, userId);
            imageRepository.save(createdImage);

            return UploadImageResponse.fromEntity(createdImage);
        }

        image.updateImageUrl(imageFlaskResponse);
        return UploadImageResponse.fromEntity(image);
    }

    private User getUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.get();
    }

    @Transactional(readOnly = true)
    public Page<ReadImageResponse> readImages(Long userId, Pageable pageable) {
        Page<Image> images = imageRepository.findByUserId(userId, pageable);
        return images.map(this::convertToReadImageResponse);
    }

    private ReadImageResponse convertToReadImageResponse(Image image) {
        return ReadImageResponse.fromEntity(image);
    }
}
