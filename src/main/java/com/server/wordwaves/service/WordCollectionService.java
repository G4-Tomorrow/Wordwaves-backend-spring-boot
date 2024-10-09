package com.server.wordwaves.service;

import com.server.wordwaves.dto.request.vocabulary.WordCollectionCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.WordCollectionCreationResponse;

public interface WordCollectionService {
    WordCollectionCreationResponse create(WordCollectionCreationRequest request);
}
