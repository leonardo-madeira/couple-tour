package com.gableo.coupletour.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class GcpStorageService {

    @Value("${gcp.bucket.name}")
    private String bucketName;

    public String uploadImagem(MultipartFile arquivo) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + arquivo.getOriginalFilename();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(".gcp/couple-tour-project-c905769bc828.json")))
                .build()
                .getService();

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(arquivo.getContentType()).build();

        storage.create(blobInfo, arquivo.getBytes());

        return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
    }
}
