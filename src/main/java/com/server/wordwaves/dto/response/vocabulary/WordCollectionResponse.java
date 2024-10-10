package com.server.wordwaves.dto.response.vocabulary;

import java.time.Instant;

import com.server.wordwaves.entity.vocabulary.WordCollectionCategory;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordCollectionResponse {
    String id;
    String name;
    String thumbnailName;
    WordCollectionCategory wordCollectionCategory;
    Instant createdAt;
    Instant updatedAt;
    String createdById;
}
