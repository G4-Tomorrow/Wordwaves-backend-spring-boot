package com.server.wordwaves.mapper;

import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.entity.vocabulary.Topic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    Topic toTopic(TopicCreationRequest request);


    TopicResponse toTopicResponse(Topic topic);
}
