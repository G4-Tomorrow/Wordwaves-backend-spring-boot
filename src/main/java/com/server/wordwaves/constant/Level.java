package com.server.wordwaves.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Level {
    NOT_RETAINED("NOT_RETAINED", 0),
    BEGINNER("BEGINNER", 1),
    LEARNING("LEARNING", 2),
    FAMILIAR("FAMILIAR", 3),
    LEARNED("LEARNED", 4),
    PROFICIENT("PROFICIENT", 5);
    String name;
    int score;

    Level(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
