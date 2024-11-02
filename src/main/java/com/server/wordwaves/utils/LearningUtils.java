package com.server.wordwaves.utils;

import com.server.wordwaves.constant.Level;

import java.time.Instant;

public class LearningUtils {
    public static Instant calculateNextPreviewTiming(int score) {
        Level level = Level.values()[score];
        return Instant.now().plus(level.getHours().multipliedBy(score));
    }

    public static Level getCurrentLevel(int score) {
        if(score > 5) return Level.PROFICIENT;
        return Level.values()[score];
    }
}
