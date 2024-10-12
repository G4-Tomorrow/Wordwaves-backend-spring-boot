package com.server.wordwaves.service.implement;

import org.springframework.stereotype.Service;

import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.entity.vocabulary.Topic;
import com.server.wordwaves.mapper.TopicMapper;
import com.server.wordwaves.repository.TopicRepository;
import com.server.wordwaves.service.TopicService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicServiceImp implements TopicService {
    TopicRepository topicRepository;
    TopicMapper topicMapper;

    @Override
    public TopicResponse create(TopicCreationRequest request) {
        Topic topic = topicMapper.toTopic(request);
        return topicMapper.toTopicResponse(topicRepository.save(topic));
    }
}
