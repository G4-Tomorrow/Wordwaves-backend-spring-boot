package com.server.wordwaves.mapper;

import org.mapstruct.Mapper;

import com.server.wordwaves.dto.request.vocabulary.WordCollectionCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.WordCollectionResponse;
import com.server.wordwaves.entity.vocabulary.WordCollection;

@Mapper(componentModel = "spring")
public interface WordCollectionMapper {
    WordCollection toWordCollection(WordCollectionCreationRequest request);

    WordCollectionResponse toWordCollectionResponse(WordCollection wordCollection);
}
