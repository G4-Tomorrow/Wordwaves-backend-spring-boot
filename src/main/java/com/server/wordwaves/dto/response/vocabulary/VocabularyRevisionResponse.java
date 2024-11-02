package com.server.wordwaves.dto.response.vocabulary;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
