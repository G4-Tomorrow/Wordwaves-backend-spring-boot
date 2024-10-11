package com.server.wordwaves.service.implement;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.server.wordwaves.dto.request.file.FileUploadRequest;
import com.server.wordwaves.dto.response.file.FileUploadResponse;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.service.FirebaseStorageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FirebaseStorageServiceImp implements FirebaseStorageService {

    @Override
    public FileUploadResponse uploadFile(FileUploadRequest request) {
        Bucket bucket = StorageClient.getInstance().bucket();

        MultipartFile file = request.getFile();
        if (file == null || file.isEmpty()) throw new AppException(ErrorCode.EMPTY_FILE);

        String filename = UUID.randomUUID().toString() + new Random().nextInt(1000);

        Blob blob;
        try {
            blob = bucket.create(filename, file.getBytes(), file.getContentType());
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAIL);
        }

        return FileUploadResponse.builder().fileName(blob.getName()).build();
    }
}
