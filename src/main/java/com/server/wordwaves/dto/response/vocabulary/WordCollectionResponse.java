package com.server.wordwaves.dto.response.vocabulary;

import com.server.wordwaves.dto.response.common.BaseAuthorResponse;
import com.server.wordwaves.entity.vocabulary.WordCollectionCategory;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordCollectionResponse extends BaseAuthorResponse {
    String id;
    String name;
    String thumbnailName;
    int numOfTotalWords;
    int numOfLearningWord;
    int numOfLearnedWord;
    WordCollectionCategory wordCollectionCategory;
}
