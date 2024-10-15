package com.server.wordwaves.dto.request.vocabulary;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "Cấp độ B1")
    String name;

    String thumbnailName;

    @NotBlank(message = "WORD_COLLECTION_CATEGORY_IS_REQUIRED")
    @Schema(example = "Từ vựng CERF")
    String category;
}
