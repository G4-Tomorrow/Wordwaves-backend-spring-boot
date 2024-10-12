package com.server.wordwaves.mapper;

import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import com.server.wordwaves.entity.vocabulary.Word;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WordMapper {
    Word toWord (WordCreationRequest request);
    WordResponse toWordResponse(Word word);
}
