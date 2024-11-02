package com.server.wordwaves.mapper;

import com.server.wordwaves.dto.request.vocabulary.WordProcessUpdateRequest;
import com.server.wordwaves.dto.response.vocabulary.WordProcessUpdateResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LearningMapper {
    WordProcessUpdateResponse toWordProcessUpdateResponse(WordProcessUpdateRequest request);
}
