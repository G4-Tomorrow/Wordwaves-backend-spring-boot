package com.server.wordwaves.entity.vocabulary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.wordwaves.constant.Level;
import com.server.wordwaves.entity.common.BaseEntity;
import com.server.wordwaves.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@Table(name = "WordInLearning", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"UserId", "WordId"})
})
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
