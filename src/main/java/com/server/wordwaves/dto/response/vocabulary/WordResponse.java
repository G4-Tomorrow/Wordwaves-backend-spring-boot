package com.server.wordwaves.dto.response.vocabulary;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.wordwaves.dto.response.common.BaseAuthorResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WordResponse extends BaseAuthorResponse {
    String id;
    String name;
    String thumbnailUrl;
    String vietnamese;
    List<PhoneticModel> phonetics;
    List<MeaningModel> meanings;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PhoneticModel {
        String text;
        String audio;
        String accent; // US - UK
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class MeaningModel {
        String partOfSpeech;
        List<DefinitionModel> definitions;
        List<String> synonyms;
        List<String> antonyms;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DefinitionModel {
        String definition;
        String definitionMeaning;

        List<String> synonyms;
        List<String> antonyms;
        String example;
        //
        String exampleMeaning;
    }
}
