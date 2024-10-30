package com.server.wordwaves.dto.response.vocabulary;

import com.server.wordwaves.constant.Level;
import com.server.wordwaves.dto.response.common.BaseAuthorResponse;
import com.server.wordwaves.dto.response.common.BaseResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
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
