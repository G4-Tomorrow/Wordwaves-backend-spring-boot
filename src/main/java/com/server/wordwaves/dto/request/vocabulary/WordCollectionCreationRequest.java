package com.server.wordwaves.dto.request.vocabulary;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordCollectionCreationRequest {
    @NotBlank(message = "WORD_COLLECTION_NAME_IS_REQUIRED")
    String name;

    String thumbnailName;
    String category;
}
