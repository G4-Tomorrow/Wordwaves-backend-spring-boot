package com.server.wordwaves.entity.vocabulary;

import jakarta.persistence.*;

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
//@Table(
//        name = "Word",
//        indexes = @Index(name = "idx_word_name", columnList = "Name")
//)
public class Word extends BaseAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String name;

    String vietnamese;

    String thumbnailUrl;
}
