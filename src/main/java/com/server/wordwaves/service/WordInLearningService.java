package com.server.wordwaves.service;

import com.server.wordwaves.dto.response.vocabulary.VocabularyLearningResponse;

public interface WordInLearningService {
    VocabularyLearningResponse learningWordCollection(String collectionId, int numOfWords);
}
