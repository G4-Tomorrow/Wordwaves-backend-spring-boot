package com.server.wordwaves.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.*;

import com.server.wordwaves.dto.request.vocabulary.TopicAddWordsRequest;
import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import com.server.wordwaves.service.TopicService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TopicController {
    TopicService topicService;

    @PostMapping
    ApiResponse<TopicResponse> create(@RequestBody @Valid TopicCreationRequest request) {
        return ApiResponse.<TopicResponse>builder()
                .message("Tạo chủ đề mới thành công")
                .result(topicService.create(request))
                .build();
    }

    @GetMapping("/get-word/{topicId}")
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

    @PutMapping("/add/{topicId}")
    ApiResponse<Void> addWords(
            @RequestBody TopicAddWordsRequest request,
            @PathVariable @NotBlank(message = "LACK_OF_PARAMETER") String topicId) {
        int size = topicService.addWords(topicId, request);
        return ApiResponse.<Void>builder()
                .message("Thêm " + size + " từ vào chủ đề thành công")
                .build();
    }
}
