package com.server.wordwaves.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.wordwaves.dto.request.vocabulary.TopicAddWordsRequest;
import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.request.vocabulary.TopicUpdateRequest;
import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import com.server.wordwaves.service.TopicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Topic Controller")
public class TopicController {
    TopicService topicService;

    @PostMapping
    @Operation(summary = "CREATE TOPIC")
    ApiResponse<TopicResponse> create(@RequestBody @Valid TopicCreationRequest request) {
        return ApiResponse.<TopicResponse>builder()
                .message("Tạo chủ đề mới thành công")
                .result(topicService.create(request))
                .build();
    }

    @GetMapping("/{topicId}/words")
    @Operation(summary = "GET WORDS FROM TOPIC")
    ApiResponse<PaginationInfo<List<WordResponse>>> getWords(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String searchQuery,
            @PathVariable("topicId") String topicId) {
        return ApiResponse.<PaginationInfo<List<WordResponse>>>builder()
                .message("Lấy các từ vựng theo chủ đề")
                .result(topicService.getWords(pageNumber, pageSize, sortBy, sortDirection, searchQuery, topicId))
                .build();
    }

    @GetMapping
    @Operation(summary = "GET TOPICS")
    ApiResponse<PaginationInfo<List<TopicResponse>>> getTopics(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String searchQuery,
            @RequestParam String userId) {
        return ApiResponse.<PaginationInfo<List<TopicResponse>>>builder()
                .message("Lấy từ vựng")
                .result(topicService.getTopics(pageNumber, pageSize, sortBy, sortDirection, searchQuery, userId))
                .build();
    }

    @PostMapping("/{topicId}/words")
    @Operation(summary = "ADD WORDS INTO TOPIC")
    ApiResponse<List<String>> addWords(
            @RequestBody @Valid TopicAddWordsRequest request,
            @PathVariable @NotBlank(message = "LACK_OF_PARAMETER") String topicId) {
        topicService.addWords(topicId, request);
        return ApiResponse.<List<String>>builder()
                .message("Thêm " + request.getWordIds().size() + " từ vào chủ đề thành công")
                .result(request.getWordIds())
                .build();
    }

    @PutMapping("/{topicId}")
    @Operation(summary = "UPDATE TOPIC BY ID")
    ApiResponse<TopicResponse> updateById(
            @PathVariable @NotBlank(message = "TOPIC_ID_IS_REQUIRED") String topicId,
            @RequestBody TopicUpdateRequest request) {
        return ApiResponse.<TopicResponse>builder()
                .message("Cập nhập thông tin chủ đề thành công")
                .result(topicService.updateById(topicId, request))
                .build();
    }

    @DeleteMapping("/{topicId}")
    @Operation(summary = "DELETE TOPIC BY ID")
    ApiResponse<Void> deleteById(@PathVariable @NotBlank(message = "TOPIC_ID_IS_REQUIRED") String topicId) {
        topicService.deleteById(topicId);
        return ApiResponse.<Void>builder().message("Xóa chủ đề thành công").build();
    }
}
