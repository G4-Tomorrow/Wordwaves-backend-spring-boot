package com.server.wordwaves.utils;

import com.server.wordwaves.constant.LearningType;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    public static LearningType getRandomLearningType() {
        LearningType[] types = LearningType.values();
        int randomIndex = ThreadLocalRandom.current().nextInt(types.length);
        return types[randomIndex];
    }
}
