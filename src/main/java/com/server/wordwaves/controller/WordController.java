package com.server.wordwaves.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import com.server.wordwaves.service.WordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/words")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Word Controller")
public class WordController {
    WordService wordService;

    @PostMapping
    @Operation(summary = "CREATE WORD")
    ApiResponse<WordResponse> create(@RequestBody @Valid WordCreationRequest request) {
        return ApiResponse.<WordResponse>builder()
                .message("Tạo từ vựng mới thành công")
                .result(wordService.create(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "GET WORDS")
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

    @DeleteMapping("/{wordId}")
    ApiResponse<Void> deleteById(@PathVariable @NotBlank(message = "INVALID_WORD_ID") String wordId) {
        wordService.deleteById(wordId);
        return ApiResponse.<Void>builder().message("Xóa từ vựng thành công").build();
    }
}
