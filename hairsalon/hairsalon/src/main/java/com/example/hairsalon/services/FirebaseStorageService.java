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

    public String uploadFileStylistAvatar(MultipartFile file, String folderPath) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        String fullPath = folderPath + fileName;

        BlobId blobId = BlobId.of(BUCKET_NAME, fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());
        storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        logger.info("File " + fullPath + " uploaded successfully to bucket: " + BUCKET_NAME);
        return "https://storage.googleapis.com/" + BUCKET_NAME + "/stylist_avatar/" + fileName;
    }

    public void deleteFile(String fileUrl) {
        try {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.indexOf("?"));
            String fullPath = "services/" + fileName;

            BlobId blobId = BlobId.of(BUCKET_NAME, fullPath);
            boolean deleted = storage.delete(blobId);

            if (deleted) {
                logger.info("File " + fullPath + " was deleted from bucket: " + BUCKET_NAME);
            } else {
                logger.warning("File " + fullPath + " was not found in bucket: " + BUCKET_NAME);
            }
        } catch (Exception e) {
            logger.severe("Error deleting file: " + e.getMessage());
        }
    }

    public void deleteFileStylist(String fileUrl) {
        try {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.indexOf("?"));
            String fullPath = "stylist_avatar/" + fileName;

            BlobId blobId = BlobId.of(BUCKET_NAME, fullPath);
            boolean deleted = storage.delete(blobId);

            if (deleted) {
                logger.info("File " + fullPath + " was deleted from bucket: " + BUCKET_NAME);
            } else {
                logger.warning("File " + fullPath + " was not found in bucket: " + BUCKET_NAME);
            }
        } catch (Exception e) {
            logger.severe("Error deleting file: " + e.getMessage());
        }
    }
}