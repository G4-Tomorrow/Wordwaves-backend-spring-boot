package com.server.wordwaves.controller;

import java.util.List;

import com.google.protobuf.Api;
import com.server.wordwaves.dto.response.vocabulary.VocabularyRevisionResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.*;

import com.server.wordwaves.dto.request.vocabulary.WordProcessUpdateRequest;
import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.vocabulary.VocabularyLearningResponse;
import com.server.wordwaves.dto.response.vocabulary.WordProcessUpdateResponse;
import com.server.wordwaves.service.WordInLearningService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/learning")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Learning Controller")
public class LearningController {
    WordInLearningService wordInLearningService;

    @GetMapping("/collections/{collectionId}/learn")
    @Operation(summary = "HỌC 1 BỘ TỪ VỰNG")
    ApiResponse<VocabularyLearningResponse> learningWordCollection(
            @PathVariable("collectionId") @NotBlank(message = "WORD_COLLECTION_ID_IS_REQUIRED") String collectionId,
            @RequestParam @Min(value = 1) int numOfWords) {
        return ApiResponse.<VocabularyLearningResponse>builder()
                .message("Học bộ từ vựng")
                .result(wordInLearningService.learningWordCollection(collectionId, numOfWords))
                .build();
    }

    @GetMapping("/topics/{topicId}/learn")
    @Operation(summary = "HỌC 1 CHỦ ĐỀ")
    ApiResponse<VocabularyLearningResponse> learningTopic(
            @PathVariable("topicId") @NotBlank(message = "TOPIC_ID_IS_REQUIRED") String topicId,
            @RequestParam @Min(value = 1) int numOfWords) {
        return ApiResponse.<VocabularyLearningResponse>builder()
                .message("Học bộ từ vựng")
                .result(wordInLearningService.learningTopic(topicId, numOfWords))
                .build();
    }

    @GetMapping("/collections/{collectionId}/review")
    @Operation(summary = "ÔN LUYỆN BỘ TỪ VỰNG")
    ApiResponse<VocabularyRevisionResponse> reviewWordCollection(
            @PathVariable("collectionId") @NotBlank(message = "WORD_COLLECTION_ID_IS_REQUIRED") String collectionId,
            @RequestParam @Min(value = 1) int numOfWords) {
        return ApiResponse.<VocabularyRevisionResponse>builder()
                .message("Ôn luyện bộ từ vựng")
                .result(wordInLearningService.reviewWordCollection(collectionId, numOfWords))
                .build();
    }

    @PatchMapping
    @Operation(summary = "CẬP NHẬP TIẾN TRÌNH HỌC TỪ VỰNG")
    ApiResponse<List<WordProcessUpdateResponse>> updateProcess(@RequestBody List<WordProcessUpdateRequest> words) {
        return ApiResponse.<List<WordProcessUpdateResponse>>builder()
                .message("Cập nhập tiến độ học từ vựng")
                .result(wordInLearningService.updateProcess(words))
                .build();
    }
}
