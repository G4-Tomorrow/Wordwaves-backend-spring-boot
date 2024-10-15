package com.server.wordwaves.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.server.wordwaves.dto.request.vocabulary.WordCollectionCreationRequest;
import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordCollectionResponse;
import com.server.wordwaves.service.WordCollectionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Word Collection Controller")
public class WordCollectionController {
    WordCollectionService wordCollectionService;

    @PostMapping
    @Operation(summary = "CREATE WORD COLLECTION")
    ApiResponse<WordCollectionResponse> create(@RequestBody @Valid WordCollectionCreationRequest request) {
        return ApiResponse.<WordCollectionResponse>builder()
                .message("Tạo bộ từ vựng thành công")
                .result(wordCollectionService.create(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "GET COLLECTIONS")
    ApiResponse<PaginationInfo<List<WordCollectionResponse>>> getCollections(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) String userId) {
        return ApiResponse.<PaginationInfo<List<WordCollectionResponse>>>builder()
                .message("Lấy nhiều bộ từ vựng")
                .result(wordCollectionService.getCollections(
                        pageNumber, pageSize, sortBy, sortDirection, searchQuery, userId))
                .build();
    }

    @GetMapping("/get-topic/{collectionId}")
    @Operation(summary = "GET TOPICS FROM WORD COLLECTION")
    ApiResponse<PaginationInfo<List<TopicResponse>>> getTopics(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String searchQuery,
            @PathVariable("collectionId") String collectionId) {
        return ApiResponse.<PaginationInfo<List<TopicResponse>>>builder()
                .message("Lấy các chủ đề của bộ từ vựng")
                .result(wordCollectionService.getTopics(
                        pageNumber, pageSize, sortBy, sortDirection, searchQuery, collectionId))
                .build();
    }
}
