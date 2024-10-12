package com.server.wordwaves.controller;

import com.google.protobuf.Api;
import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import com.server.wordwaves.service.WordService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/words")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WordController {
    WordService wordService;

    @PostMapping
    ApiResponse<WordResponse> create(@RequestBody @Valid WordCreationRequest request) {
        return ApiResponse.<WordResponse>builder()
                .message("Tạo từ vựng mới thành công")
                .result(wordService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<PaginationInfo<List<WordResponse>>> getWords(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String searchQuery) {
        return ApiResponse.<PaginationInfo<List<WordResponse>>>builder()
                .message("Lấy từ vựng")
                .result(wordService.getWords(pageNumber, pageSize, sortBy, sortDirection, searchQuery))
                .build();
    }
}
