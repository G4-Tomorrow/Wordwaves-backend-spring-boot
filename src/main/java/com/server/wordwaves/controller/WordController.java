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
import com.server.wordwaves.dto.request.vocabulary.WordUpdateRequest;
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
    @Operation(summary = "TẠO TỪ VỰNG")
    ApiResponse<WordResponse> create(@RequestBody @Valid WordCreationRequest request) {
        return ApiResponse.<WordResponse>builder()
                .message("Tạo từ vựng mới thành công")
                .result(wordService.create(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "LẤY TỪ VỰNG")
    ApiResponse<PaginationInfo<List<WordResponse>>> getWords(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String searchQuery,
            @RequestParam String userId,
            @RequestParam(required = false) String isUnassigned) { // Thêm tham số mới
        log.info("assign: {}", isUnassigned);
        return ApiResponse.<PaginationInfo<List<WordResponse>>>builder()
                .message("Lấy từ vựng")
                .result(wordService.getWords(
                        pageNumber, pageSize, sortBy, sortDirection, searchQuery, userId, isUnassigned))
                .build();
    }

    @GetMapping("/{name}")
    @Operation(summary = "LẤY THÔNG TIN CHI TIẾT CỦA TỪ VỰNG")
    ApiResponse<WordResponse> detail(@PathVariable("name") @NotBlank(message = "WORD_ID_IS_REQUIRED") String name) {
        return ApiResponse.<WordResponse>builder()
                .message("Lấy thông tin chi tiết từ vựng")
                .result(wordService.detail(name))
                .build();
    }

    @PutMapping("/{wordId}")
    @Operation(summary = "CẬP NHẬP TỪ VỰNG QUA ID")
    ApiResponse<WordResponse> updateById(
            @PathVariable @NotBlank(message = "WORD_ID_IS_REQUIRED") String wordId,
            @RequestBody WordUpdateRequest request) {
        return ApiResponse.<WordResponse>builder()
                .message("Cập nhập từ vựng thành công")
                .result(wordService.updateById(wordId, request))
                .build();
    }

    @DeleteMapping("/{wordId}")
    @Operation(summary = "XÓA TỪ VỰNG QUA ID")
    ApiResponse<Void> deleteById(@PathVariable @NotBlank(message = "INVALID_WORD_ID") String wordId) {
        wordService.deleteById(wordId);
        return ApiResponse.<Void>builder().message("Xóa từ vựng thành công").build();
    }
}
