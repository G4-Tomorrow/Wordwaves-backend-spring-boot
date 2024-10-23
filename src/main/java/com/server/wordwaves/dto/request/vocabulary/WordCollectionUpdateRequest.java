package com.server.wordwaves.dto.request.vocabulary;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordCollectionUpdateRequest {
    String name;
    String thumbnailName;
    String category;
}
