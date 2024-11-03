package com.server.wordwaves.utils;

import java.util.concurrent.ThreadLocalRandom;

import com.server.wordwaves.constant.LearningType;

public class RandomUtils {
    public static LearningType getRandomLearningType() {
        LearningType[] types = LearningType.values();
        int randomIndex = ThreadLocalRandom.current().nextInt(types.length);
        return types[randomIndex];
    }
}
