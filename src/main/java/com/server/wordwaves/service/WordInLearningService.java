package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.vocabulary.WordProcessUpdateRequest;
import com.server.wordwaves.dto.response.vocabulary.VocabularyLearningResponse;
import com.server.wordwaves.dto.response.vocabulary.VocabularyRevisionResponse;
import com.server.wordwaves.dto.response.vocabulary.WordProcessUpdateResponse;

public interface WordInLearningService {
    VocabularyLearningResponse learningWordCollection(String collectionId, int numOfWords);

    List<WordProcessUpdateResponse> updateProcess(List<WordProcessUpdateRequest> words);

    VocabularyLearningResponse learningTopic(String topicId, int numOfWords);

    VocabularyRevisionResponse reviewWordCollection(String collectionId, int numOfWords);

    VocabularyRevisionResponse reviewTopic(String topicId, int numOfWords);
}
