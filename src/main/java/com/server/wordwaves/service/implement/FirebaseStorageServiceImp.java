package com.server.wordwaves.service.implement;

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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FirebaseStorageServiceImp implements FirebaseStorageService {

    @Override
    public FileUploadResponse uploadFile(FileUploadRequest request) {
        Bucket bucket = StorageClient.getInstance().bucket();
        MultipartFile file = request.getFile();
        String filename = UUID.randomUUID().toString() + new Random().nextInt(1000);

        Blob blob;
        try {
            blob = bucket.create(filename, file.getBytes(), file.getContentType());
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAIL);
        }

        // Trả về đường dẫn của file đã upload
        return FileUploadResponse.builder()
                .fileName(blob.getName())
                .build();
    }
}
