package com.server.wordwaves.dto.response.vocabulary;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VocabularyRevisionResponse {
    int numOfWords;
    int numOfReviewedWords;

    List<WordInLearningResponse> words;
}
