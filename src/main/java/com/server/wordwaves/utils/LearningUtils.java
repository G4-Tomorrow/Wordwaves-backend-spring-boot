package com.server.wordwaves.utils;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.server.wordwaves.constant.Level;
import com.server.wordwaves.dto.response.vocabulary.WordInLearningResponse;

public class LearningUtils {
    public static Instant calculateNextPreviewTiming(int score) {
        Level level = Level.values()[score];
        return Instant.now().plus(level.getHours().multipliedBy(Math.max(score, 1)));
    }

    public static Level getCurrentLevel(int score) {
        if (score > 6) return Level.MASTER;
        return Level.values()[score];
    }

    public static List<WordInLearningResponse> toWordInLearningResponses(List<Object[]> wordInforms) {
        // Chuyển đổi các từ thành đối tượng response
        return wordInforms.stream()
                .map(wordInform -> WordInLearningResponse.builder()
                        .id((String) wordInform[1])
                        .word((String) wordInform[0])
                        .learningType(RandomUtils.getRandomLearningType())
                        .build())
                .collect(Collectors.toList());
    }
}
