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
public class WordCollectionAddTopicsRequest {
    @Schema(example = "[\"Bộ từ vựng 1\", \"Bộ từ vựng 2\", \"Bộ từ vựng 3\"]")
    @NotNull(message = "INVALID_TOPIC_IDS")
    List<String> topicIds;
}
