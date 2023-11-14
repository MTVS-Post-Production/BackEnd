package com.alal.backend.service.user;

import com.alal.backend.domain.dto.request.UploadMemoRequest;
import com.alal.backend.domain.dto.response.ReadMemoResponse;
import com.alal.backend.domain.dto.response.UploadMemoResponse;
import com.alal.backend.domain.entity.user.Memo;
import com.alal.backend.domain.entity.user.User;
import com.alal.backend.domain.entity.user.vo.Group;
import com.alal.backend.repository.user.MemoRepository;
import com.alal.backend.repository.user.UserRepository;
import com.alal.backend.utils.Parsor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CsvService {
    private final MemoRepository memoRepository;
    private final UserRepository userRepository;
    private final Parsor parsor;

    @Transactional
    public UploadMemoResponse uploadMemo(UploadMemoRequest uploadMemoRequest) {
        try {
            List<Memo> memos = parsor.parseCsv(uploadMemoRequest);
            memoRepository.saveAll(memos);

            return UploadMemoResponse.fromEntity(memos);
        } catch (IOException e) {
            throw new RuntimeException("Csv 파일 업로드 중 오류가 발생하였습니다.");
        }
    }

    @Transactional(readOnly = true)
    public Page<ReadMemoResponse> readMemos(Long userId, Pageable pageable) {
        User user = getUser(userId);
        Group group = getUserGroup(user);
        Page<Memo> memos = memoRepository.findAllByGroup(group, pageable);

        return memos.map(this::convertToReadMemoResponse);
    }

    private Group getUserGroup(User user) {
        return Group.fromUser(user.getUserGroup());
    }

    private User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.get();
    }

    private ReadMemoResponse convertToReadMemoResponse(Memo memo) {
        return ReadMemoResponse.fromEntity(memo);
    }
}
