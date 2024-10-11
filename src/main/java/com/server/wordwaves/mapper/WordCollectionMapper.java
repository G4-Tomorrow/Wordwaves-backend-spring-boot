package com.server.wordwaves.mapper;

import org.mapstruct.Mapper;

import com.server.wordwaves.dto.request.vocabulary.WordCollectionCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.WordCollectionResponse;
import com.server.wordwaves.entity.vocabulary.WordCollection;

@Mapper(componentModel = "spring")
public interface WordCollectionMapper {
    WordCollection toWordCollection(WordCollectionCreationRequest request);

    //    @Mapping(target = "createdAt", source = "createdAt")
    //    @Mapping(target = "updatedAt", source = "updatedAt")
    //    @Mapping(target = "createdById", source = "createdById")
    WordCollectionResponse toWordCollectionResponse(WordCollection wordCollection);
}
