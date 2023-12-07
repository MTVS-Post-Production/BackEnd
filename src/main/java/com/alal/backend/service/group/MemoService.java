package com.alal.backend.service.group;

import com.alal.backend.domain.dto.request.UploadMemoRequest;
import com.alal.backend.domain.dto.response.ReadMemoResponse;
import com.alal.backend.domain.dto.response.UploadMemoResponse;
import com.alal.backend.domain.entity.project.Memo;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.domain.vo.Group;
import com.alal.backend.repository.group.MemoRepository;
import com.alal.backend.repository.user.UserRepository;
import com.alal.backend.utils.Parser;
import com.alal.backend.utils.event.UploadRollBackEvent;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final UserRepository userRepository;

    private final Parser parser;
    private final Storage storage;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    private static final String MEMO_FOLDER = "Memo/";

    @Transactional
    public UploadMemoResponse uploadMemo(UploadMemoRequest uploadMemoRequest, Long userId) {
        User user = getUser(userId);

        return saveMemo(uploadMemoRequest, user);
    }

    private UploadMemoResponse saveMemo(UploadMemoRequest uploadMemoRequest, User user) throws StorageException {
        Memo memo = memoRepository.findByGroup(getUserGroup(user));
        if (memo != null) {
            memoRepository.delete(memo);
        }

        String uploadUrl = uploadStorage(user, uploadMemoRequest);
        eventPublisher.publishEvent(new UploadRollBackEvent(bucketName, uploadUrl));
        Memo createdMemo = Memo.fromEntity(uploadUrl, user.getUserGroup());
        memoRepository.save(createdMemo);

        return UploadMemoResponse.fromEntity(uploadUrl);
    }

    private String uploadStorage(User user, UploadMemoRequest uploadMemoRequest) {
        byte[] decodedBytes = Base64.getDecoder().decode(uploadMemoRequest.getCsvFile());
        String memoUrl = MEMO_FOLDER + user.getUserGroup() + ".csv";

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, memoUrl)
                        .setContentType("text/csv")
                        .build(),
                decodedBytes
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
