package com.server.wordwaves.dto.request.vocabulary;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicAddWordsRequest {
    @Schema(example = "[\"hello\", \"an\", \"word\"]")
    @NotNull(message = "INVALID_WORD_IDS")
    List<String> wordIds;
}
