package com.server.wordwaves.service;

import com.server.wordwaves.dto.request.file.FileUploadRequest;
import com.server.wordwaves.dto.response.file.FileUploadResponse;

public interface FirebaseStorageService {
    FileUploadResponse uploadFile(FileUploadRequest request);
}
