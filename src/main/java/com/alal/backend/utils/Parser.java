package com.alal.backend.utils;

import com.google.cloud.storage.BlobInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class Parser {
    public String parseBlobInfo(BlobInfo blobInfo) {
        String[] splitName = blobInfo.getName().split(" ");
        StringBuilder encodedName = new StringBuilder();

        for (String part : splitName) {
            encodedName.append(URLEncoder.encode(part, StandardCharsets.UTF_8));
            encodedName.append("%20");
        }

        encodedName.setLength(encodedName.length() - 3);

        return String.format("https://storage.googleapis.com/%s/%s", blobInfo.getBucket(), encodedName.toString());
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
