package com.server.wordwaves.dto.model.vocabulary;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefinitionModel {
    String definition;
    String example;
    List<String> synonyms;
    List<String> antonyms;
}
