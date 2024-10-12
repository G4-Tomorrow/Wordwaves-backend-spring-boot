package com.server.wordwaves.mapper;

import org.mapstruct.Mapper;

import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import com.server.wordwaves.entity.vocabulary.Word;

@Mapper(componentModel = "spring")
public interface WordMapper {
    Word toWord(WordCreationRequest request);

    WordResponse toWordResponse(Word word);
}
