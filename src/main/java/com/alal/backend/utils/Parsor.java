package com.alal.backend.utils;

import com.alal.backend.domain.entity.user.Memo;
import com.alal.backend.domain.dto.request.UploadMemoRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class Parsor {
    public List<Memo> parseCsv(UploadMemoRequest uploadMemoRequest) throws IOException {
        InputStream inputStream = uploadMemoRequest.getCsvFile().getInputStream();

        try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream, StandardCharsets.UTF_8), CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            List<Memo> memos = new ArrayList<>();

            for (CSVRecord csvRecord : csvParser) {
                String author = csvRecord.get("author");
                String content = csvRecord.get("content");
                String writtenAt = csvRecord.get("createdAt");
                String groupName = csvRecord.get("groupName");

                Memo memo = Memo.toEntity(author, content, writtenAt, groupName);
                memos.add(memo);
            }

            return memos;
        }
    }
}
