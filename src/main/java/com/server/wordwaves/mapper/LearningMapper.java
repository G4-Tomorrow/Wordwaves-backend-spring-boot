package com.server.wordwaves.mapper;

import org.mapstruct.Mapper;

import com.server.wordwaves.dto.request.vocabulary.WordProcessUpdateRequest;
import com.server.wordwaves.dto.response.vocabulary.WordProcessUpdateResponse;

@Mapper(componentModel = "spring")
public interface LearningMapper {
    WordProcessUpdateResponse toWordProcessUpdateResponse(WordProcessUpdateRequest request);
}
