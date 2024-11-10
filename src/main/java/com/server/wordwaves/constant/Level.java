package com.server.wordwaves.constant;

import java.time.Duration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Level {
    NOT_RETAINED("NOT_RETAINED", 0, Duration.ofSeconds(2)),
    BEGINNER("BEGINNER", 1, Duration.ofSeconds(4)),
    LEARNING("LEARNING", 2, Duration.ofSeconds(6)),
    FAMILIAR("FAMILIAR", 3, Duration.ofSeconds(8)),
    LEARNED("LEARNED", 4, Duration.ofSeconds(10)),
    PROFICIENT("PROFICIENT", 5, Duration.ofSeconds(12)),
    MASTER("MASTER", 6, Duration.ofSeconds(24));

    String name;
    int score;
    Duration hours;

    Level(String name, int score, Duration hours) {
        this.name = name;
        this.score = score;
        this.hours = hours;
    }
}
