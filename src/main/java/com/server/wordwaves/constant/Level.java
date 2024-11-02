package com.server.wordwaves.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Level {
    NOT_RETAINED("NOT_RETAINED", 0, Duration.ofHours(2)),
    BEGINNER("BEGINNER", 1, Duration.ofHours(4)),
    LEARNING("LEARNING", 2, Duration.ofHours(6)),
    FAMILIAR("FAMILIAR", 3, Duration.ofHours(8)),
    LEARNED("LEARNED", 4, Duration.ofHours(10)),
    PROFICIENT("PROFICIENT", 5, Duration.ofHours(12));

    String name;
    int score;
    Duration hours;

    Level(String name, int score, Duration hours) {
        this.name = name;
        this.score = score;
        this.hours = hours;
    }
}
