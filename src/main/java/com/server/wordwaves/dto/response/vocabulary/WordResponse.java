package com.server.wordwaves.dto.response.vocabulary;

import java.util.List;

import com.server.wordwaves.dto.response.common.BaseAuthorResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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
        List<String> synonyms;
        List<String> antonyms;
        String example;
    }
}
