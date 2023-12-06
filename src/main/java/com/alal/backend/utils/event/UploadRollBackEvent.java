package com.alal.backend.utils.event;

import com.google.cloud.storage.Storage;


public class UploadRollBackEvent {
    private final String bucketName;
    private final String subPath;

    public UploadRollBackEvent(String bucketName, String url) {
        this.bucketName = bucketName;
        this.subPath = getObjectName(url);
    }

    private String getObjectName(String uploadUrl) {
        return uploadUrl.substring(uploadUrl.indexOf(this.bucketName) + this.bucketName.length() + 1);
    }

    public void deleteUrl(Storage storage) {
        storage.delete(this.bucketName, this.subPath);
    }
}
