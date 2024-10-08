package com.server.wordwaves.controller;

import com.server.wordwaves.dto.request.file.FileUploadRequest;
import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.file.FileUploadResponse;
import com.server.wordwaves.service.FirebaseStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileUploadController {
    FirebaseStorageService firebaseStorageService;

    @PostMapping
    ApiResponse<FileUploadResponse> upload(@ModelAttribute FileUploadRequest request) {
        return ApiResponse.<FileUploadResponse>builder()
                .message("Upload ảnh thành công")
                .result(firebaseStorageService.uploadFile(request))
                .build();
    }
}
