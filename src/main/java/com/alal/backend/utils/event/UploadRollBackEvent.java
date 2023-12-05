package com.alal.backend.utils.event;

import com.google.cloud.storage.Storage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UploadRollBackEvent {
    private final String bucketName;
    private final String subPath;

    public void deleteUrl(Storage storage) {
        storage.delete(bucketName, subPath);
    }
}
