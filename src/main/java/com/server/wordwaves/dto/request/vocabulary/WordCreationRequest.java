package com.server.wordwaves.dto.request.vocabulary;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordCreationRequest {
    @NotBlank(message = "WORD_NAME_IS_REQUIRED")
    String name;

    @Builder.Default
    String topicId = "";
}
