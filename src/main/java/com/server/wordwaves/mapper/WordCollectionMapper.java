package com.server.wordwaves.mapper;

import com.server.wordwaves.dto.request.vocabulary.WordCollectionCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.WordCollectionCreationResponse;
import com.server.wordwaves.entity.vocabulary.WordCollection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WordCollectionMapper {
    WordCollection toWordCollection(WordCollectionCreationRequest request);

    WordCollectionCreationResponse toWordCollectionResponse(WordCollection wordCollection);
}
