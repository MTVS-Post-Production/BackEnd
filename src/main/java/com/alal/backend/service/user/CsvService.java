package com.alal.backend.service.user;

import com.alal.backend.domain.entity.user.Memo;
import com.alal.backend.payload.request.user.UploadMemoRequest;
import com.alal.backend.payload.response.UploadMemoResponse;
import com.alal.backend.repository.user.MemoRepository;
import com.alal.backend.utils.Parsor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvService {
    private final MemoRepository memoRepository;
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
}
