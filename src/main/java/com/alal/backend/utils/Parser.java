package com.alal.backend.utils;

import com.google.cloud.storage.BlobInfo;
import org.springframework.stereotype.Service;

@Service
public class Parser {
    public String parseBlobInfo(BlobInfo blobInfo) {
        return String.format("https://storage.googleapis.com/%s/%s", blobInfo.getBucket(), blobInfo.getName());
    }
}
