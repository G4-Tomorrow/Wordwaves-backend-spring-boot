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
public class WordInLearning extends BaseEntity {
    @Id
    @GeneratedValue
    UUID id;

    @Enumerated(EnumType.STRING)
    Level level;

    String customMeaning;
    Instant nextReviewTiming;

    @Builder.Default
    int score = 0;

    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "id")
    @JsonIgnore
    User user;

    @ManyToOne
    @JoinColumn(name = "WordId", referencedColumnName = "id")
    Word word;
}
