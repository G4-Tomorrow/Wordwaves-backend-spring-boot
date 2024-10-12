package com.server.wordwaves.service;

import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;

public interface TopicService {
    TopicResponse create(TopicCreationRequest request);
}
