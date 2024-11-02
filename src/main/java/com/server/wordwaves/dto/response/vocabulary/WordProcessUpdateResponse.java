package com.server.wordwaves.dto.response.vocabulary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordProcessUpdateResponse {
    String wordId;
    @JsonProperty("isCorrect")
    Boolean isCorrect;
}
