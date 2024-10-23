package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.vocabulary.WordCollectionCreationRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordCollectionResponse;
import org.springframework.security.access.prepost.PreAuthorize;

public interface WordCollectionService {
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    WordCollectionResponse create(WordCollectionCreationRequest request);

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    PaginationInfo<List<WordCollectionResponse>> getCollections(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String userId);

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    PaginationInfo<List<TopicResponse>> getTopics(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String collectionId);
}
