package com.server.wordwaves.dto.response.vocabulary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.wordwaves.constant.LearningType;
import com.server.wordwaves.constant.Level;
import com.server.wordwaves.dto.response.common.BaseAuthorResponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordInLearningResponse extends BaseAuthorResponse {
    Level level;
    LearningType learningType;
    String customMeaning;
    int score = 0;

    WordResponse word;
}
