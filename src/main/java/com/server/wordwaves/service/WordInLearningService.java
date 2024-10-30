package com.server.wordwaves.service;

import java.util.UUID;

import com.server.wordwaves.dto.response.vocabulary.VocabularyLearningResponse;

public interface WordInLearningService {
    VocabularyLearningResponse learningWordCollection(String collectionId, int numOfWords);
}
