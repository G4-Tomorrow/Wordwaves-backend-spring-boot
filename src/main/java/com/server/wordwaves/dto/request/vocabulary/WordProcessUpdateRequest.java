package com.server.wordwaves.dto.request.vocabulary;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordProcessUpdateRequest {
    String wordId;

    @JsonProperty("isCorrect")
    Boolean isCorrect;

    @JsonProperty("isAlreadyKnow")
    Boolean isAlreadyKnow;
}
