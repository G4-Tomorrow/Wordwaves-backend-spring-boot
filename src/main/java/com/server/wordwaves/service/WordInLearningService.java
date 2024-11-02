package com.server.wordwaves.service;

import com.server.wordwaves.dto.request.vocabulary.WordProcessUpdateRequest;
import com.server.wordwaves.dto.response.vocabulary.VocabularyLearningResponse;

import java.util.List;

public interface WordInLearningService {
    VocabularyLearningResponse learningWordCollection(String collectionId, int numOfWords);

    List<WordProcessUpdateRequest> updateProcess(List<WordProcessUpdateRequest> words);
}
