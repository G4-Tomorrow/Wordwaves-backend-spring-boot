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
public class WordCreationRequest {
    @NotBlank(message = "WORD_NAME_IS_REQUIRED")
    @Schema(example = "hello")
    String name;

    @Builder.Default
    String topicId = "";
}
