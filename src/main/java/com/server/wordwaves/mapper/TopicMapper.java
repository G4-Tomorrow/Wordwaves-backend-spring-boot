package com.server.wordwaves.mapper;

import org.mapstruct.Mapper;

import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.entity.vocabulary.Topic;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    Topic toTopic(TopicCreationRequest request);

    TopicResponse toTopicResponse(Topic topic);
}
