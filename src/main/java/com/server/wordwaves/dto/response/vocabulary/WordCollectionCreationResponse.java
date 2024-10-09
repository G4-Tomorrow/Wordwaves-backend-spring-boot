package com.server.wordwaves.dto.response.vocabulary;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordCollectionCreationResponse {
    String id;
    String name;
    String thumbnailName;
}
