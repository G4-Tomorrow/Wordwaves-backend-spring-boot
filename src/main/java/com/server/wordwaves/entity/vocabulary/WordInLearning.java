package com.server.wordwaves.entity.vocabulary;

import java.time.Instant;

import jakarta.persistence.*;

import com.server.wordwaves.constant.Level;
import com.server.wordwaves.entity.common.BaseEntity;

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
@Table(
        name = "WordInLearning",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"UserId", "WordId"})})
public class WordInLearning extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Enumerated(EnumType.STRING)
    Level level;

    String customMeaning;
    Instant nextReviewTiming;

    @Builder.Default
    int score = 0;

    @Column(nullable = false)
    String userId;

    @Column(nullable = false)
    String wordId;
}
