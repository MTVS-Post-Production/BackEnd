package com.alal.backend.service.user;

import com.alal.backend.domain.dto.request.UploadMemoRequest;
import com.alal.backend.domain.dto.response.ReadMemoResponse;
import com.alal.backend.domain.dto.response.UploadMemoResponse;
import com.alal.backend.domain.entity.user.Memo;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.domain.entity.user.vo.Group;
import com.alal.backend.repository.user.MemoRepository;
import com.alal.backend.repository.user.UserRepository;
import com.alal.backend.utils.Parser;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final MemoRepository memoRepository;
    private final UserRepository userRepository;
    private final Parser parser;

    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Transactional
    public UploadMemoResponse uploadMemo(UploadMemoRequest uploadMemoRequest, Long userId) {
        User user = getUser(userId);
        String ext = uploadMemoRequest.getExt();
        String uploadUrl = uploadStorage(user, ext);

        return saveMemo(uploadUrl, user);
    }

    private UploadMemoResponse saveMemo(String uploadUrl, User user) {
        Memo memo = memoRepository.findByGroup(getUserGroup(user));

        if (memo == null) {
            Memo createdMemo = Memo.fromEntity(uploadUrl, user.getUserGroup());
            memoRepository.save(createdMemo);
        }

        return UploadMemoResponse.fromEntity(uploadUrl);
    }

    private String uploadStorage(User user, String ext) {
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, user.getUserGroup())
                        .setContentType(ext)
                        .build()
        );

        return parser.parseBlobInfo(blobInfo);
    }

    @Transactional(readOnly = true)
    public ReadMemoResponse readMemos(Long userId) {
        User user = getUser(userId);
        Group group = getUserGroup(user);

        return findMemo(group);
    }

    private ReadMemoResponse findMemo(Group group) {
        Memo memo = memoRepository.findByGroup(group);

        return ReadMemoResponse.fromEntity(memo);
    }

    private Group getUserGroup(User user) {
        return Group.fromUser(user.getUserGroup());
    }

    private User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NoSuchElementException("유저를 찾을 수 없습니다.");
        }
        return user.get();
    }
}
