package com.alal.backend.utils;

import com.google.cloud.storage.BlobInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class Parser {
    public String parseBlobInfo(BlobInfo blobInfo) {
        try {
            String encodedName = URLEncoder.encode(blobInfo.getName(), StandardCharsets.UTF_8.toString());
            return String.format("https://storage.googleapis.com/%s/%s", blobInfo.getBucket(), encodedName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL 인코딩에 실패했습니다.", e);
        }
    }

    public static String encodeImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public static String downloadAndEncodeImage(String imageUrl) throws IOException {
        byte[] imageBytes = downloadImage(imageUrl);
        return encodeImageToBase64(imageBytes);
    }

    private static byte[] downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        InputStream inputStream = url.openStream();
        byte[] imageBytes = inputStream.readAllBytes();
        inputStream.close();

        return imageBytes;
    }
}
