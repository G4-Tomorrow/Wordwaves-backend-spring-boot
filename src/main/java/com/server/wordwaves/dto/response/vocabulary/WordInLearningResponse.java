package com.server.wordwaves.dto.response.vocabulary;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.wordwaves.constant.Level;
import com.server.wordwaves.dto.response.common.BaseAuthorResponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordInLearningResponse extends BaseAuthorResponse {
    Level level;
    String customMeaning;
    Instant nextReviewTiming;
    int numOfWrongAnswers = 0;
    int numOfCorrectAnswers = 0;
    int score = 0;

    WordResponse word;
}
