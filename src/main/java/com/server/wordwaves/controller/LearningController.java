package com.server.wordwaves.controller;

import com.google.protobuf.Api;
import com.server.wordwaves.dto.request.vocabulary.WordProcessUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.vocabulary.VocabularyLearningResponse;
import com.server.wordwaves.service.WordInLearningService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/learning")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Learning Controller")
public class LearningController {
    WordInLearningService wordInLearningService;

    @GetMapping("/{collectionId}")
    @Operation(summary = "HỌC 1 BỘ TỪ VỰNG")
    ApiResponse<VocabularyLearningResponse> learningWordCollection(
            @PathVariable("collectionId") String collectionId, @RequestParam int numOfWords) {
        return ApiResponse.<VocabularyLearningResponse>builder()
                .message("Học bộ từ vựng")
                .result(wordInLearningService.learningWordCollection(collectionId, numOfWords))
                .build();
    }

    @PatchMapping
    @Operation(summary = "Cập nhập tiến trình học từ vựng")
    ApiResponse<List<WordProcessUpdateRequest>> updateProcess(List<WordProcessUpdateRequest> words) {
        return ApiResponse.<List<WordProcessUpdateRequest>>builder()
                .message("Cập nhập tiến độ học từ vựng")
                .result(wordInLearningService.updateProcess(words))
                .build();
    }
}
