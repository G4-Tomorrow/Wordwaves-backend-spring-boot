package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.vocabulary.TopicAddWordsRequest;
import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;

public interface TopicService {
    TopicResponse create(TopicCreationRequest request);

    PaginationInfo<List<WordResponse>> getWords(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String topicId);

    int addWords(String topicId, TopicAddWordsRequest request);
}
