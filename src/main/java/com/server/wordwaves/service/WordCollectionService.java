package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.vocabulary.WordCollectionAddTopicsRequest;
import com.server.wordwaves.dto.request.vocabulary.WordCollectionCreationRequest;
import com.server.wordwaves.dto.request.vocabulary.WordCollectionUpdateRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordCollectionResponse;

public interface WordCollectionService {
    WordCollectionResponse create(WordCollectionCreationRequest request);

    PaginationInfo<List<WordCollectionResponse>> getCollections(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String userId);

    PaginationInfo<List<TopicResponse>> getTopics(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String collectionId);

    WordCollectionResponse updateById(String collectionId, WordCollectionUpdateRequest request);

    void deleteById(String collectionId);

    void addTopics(String collectionId, WordCollectionAddTopicsRequest request);
}
