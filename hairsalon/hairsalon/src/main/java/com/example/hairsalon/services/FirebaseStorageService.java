package com.example.hairsalon.services;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

@Service
public class FirebaseStorageService {
    private static final Logger logger = Logger.getLogger(FirebaseStorageService.class.getName());
    private static final String BUCKET_NAME = "hair-salon-e9f1c.appspot.com";

    private final Storage storage;

    @Autowired
    public FirebaseStorageService(Storage storage) {
        this.storage = storage;
    }

    public String uploadFile(MultipartFile file, String folderPath) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        String fullPath = folderPath + fileName;

        BlobId blobId = BlobId.of(BUCKET_NAME, fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());
        storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        logger.info("File " + fullPath + " uploaded successfully to bucket: " + BUCKET_NAME);
        return "https://storage.googleapis.com/" + BUCKET_NAME + "/services/" + fileName;
    }
}