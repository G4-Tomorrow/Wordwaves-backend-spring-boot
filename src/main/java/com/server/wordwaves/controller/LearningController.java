package com.server.wordwaves.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.vocabulary.VocabularyLearningResponse;
import com.server.wordwaves.service.WordInLearningService;

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

    @GetMapping("/{collectionId}")
    ApiResponse<VocabularyLearningResponse> learningWordCollection(
            @PathVariable("collectionId") String collectionId, @RequestParam int numOfWords) {
        return ApiResponse.<VocabularyLearningResponse>builder()
                .message("Học bộ từ vựng")
                .result(wordInLearningService.learningWordCollection(collectionId, numOfWords))
                .build();
    }
}
