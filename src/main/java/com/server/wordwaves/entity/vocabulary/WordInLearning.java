package com.server.wordwaves.entity.vocabulary;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

import com.server.wordwaves.constant.Level;
import com.server.wordwaves.entity.common.BaseAuthor;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class WordInLearning extends BaseAuthor {
    @Id
    @GeneratedValue
    UUID id;

    @Enumerated(EnumType.STRING)
    Level level;

    String customMeaning;
    Instant nextReviewTiming;

    @Builder.Default
    int numOfWrongAnswers = 0;

    @Builder.Default
    int numOfCorrectAnswers = 0;

    @Builder.Default
    int score = 0;

    @ManyToOne
    @JoinColumn(name = "WordId", referencedColumnName = "id")
    Word word;
}
