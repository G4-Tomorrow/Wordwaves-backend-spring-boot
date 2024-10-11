package com.server.wordwaves.controller;

import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.service.TopicService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
