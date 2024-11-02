package com.server.wordwaves.utils;

import com.server.wordwaves.constant.Level;
import com.server.wordwaves.dto.response.vocabulary.WordInLearningResponse;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class LearningUtils {
    public static Instant calculateNextPreviewTiming(int score) {
        Level level = Level.values()[score];
        return Instant.now().plus(level.getHours().multipliedBy(Math.max(score, 1)));
    }

    public static Level getCurrentLevel(int score) {
        if (score > 5) return Level.PROFICIENT;
        return Level.values()[score];
    }

    public static List<WordInLearningResponse> toWordInLearningResponses(List<String> wordIds) {
        // Chuyển đổi các từ thành đối tượng response
        return wordIds.stream()
                .map(word -> WordInLearningResponse.builder()
                        .wordId(word)
                        .learningType(RandomUtils.getRandomLearningType())
                        .build())
                .collect(Collectors.toList());
    }
}
