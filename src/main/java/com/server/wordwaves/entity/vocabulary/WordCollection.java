package com.server.wordwaves.entity.vocabulary;

import java.util.Set;

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
public class WordCollection extends BaseAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String name;

    String thumbnailName;

    @Builder.Default
    int numOfTotalWords = 0;

    @Builder.Default
    int numOfLearningWord = 0;

    @Builder.Default
    int numOfLearnedWord = 0;

    @ManyToOne
    @JoinColumn(name = "WordCollectionCategoryId", referencedColumnName = "id")
    WordCollectionCategory wordCollectionCategory;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "WordCollectionToTopic",
            joinColumns = @JoinColumn(name = "WordCollectionId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "TopicId", referencedColumnName = "id"))
    Set<Topic> topics;
}
