package com.server.wordwaves.controller;

import com.server.wordwaves.dto.response.vocabulary.WordCollectionCreationResponse;
import com.server.wordwaves.service.WordCollectionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.wordwaves.dto.request.vocabulary.WordCollectionCreationRequest;
import com.server.wordwaves.dto.response.common.ApiResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WordCollectionController {
    WordCollectionService wordCollectionService;

    @PostMapping
    ApiResponse<WordCollectionCreationResponse> create(@RequestBody WordCollectionCreationRequest request) {
        return ApiResponse.<WordCollectionCreationResponse>builder()
                .message("Tạo bộ từ vựng thành công")
                .result(wordCollectionService.create(request))
                .build();
    }
}
