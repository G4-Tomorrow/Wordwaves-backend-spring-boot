package com.server.wordwaves.service;

import com.server.wordwaves.dto.request.file.FileUploadRequest;
import com.server.wordwaves.dto.response.file.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FirebaseStorageService {
    FileUploadResponse uploadFile(FileUploadRequest request);
}
