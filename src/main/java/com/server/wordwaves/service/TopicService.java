package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.vocabulary.TopicAddWordsRequest;
import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import org.springframework.security.access.prepost.PreAuthorize;

public interface TopicService {
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    TopicResponse create(TopicCreationRequest request);

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    PaginationInfo<List<WordResponse>> getWords(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String topicId);

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    int addWords(String topicId, TopicAddWordsRequest request);
}
