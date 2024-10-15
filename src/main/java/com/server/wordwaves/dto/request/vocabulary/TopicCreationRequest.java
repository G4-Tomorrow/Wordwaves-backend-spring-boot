package com.server.wordwaves.dto.request.vocabulary;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicCreationRequest {
    @NotBlank(message = "TOPIC_NAME_IS_REQUIRED")
    @Schema(example = "Khoa học tự nhiên")
    String name;

    @NotBlank(message = "TOPIC_MUST_BELONG_TO_WORD_COLLECTION")
    String collectionId;

    String thumbnailName;
}
