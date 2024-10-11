package com.server.wordwaves.entity.vocabulary;

import java.util.Set;

import jakarta.persistence.*;

import com.server.wordwaves.entity.common.BaseAuthor;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class WordCollection extends BaseAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String name;

    String thumbnailName;

    @ManyToOne
    @JoinColumn(name = "WordCollectionCategoryId", referencedColumnName = "id")
    WordCollectionCategory wordCollectionCategory;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "CollectionToTopic",
            joinColumns = @JoinColumn(name = "WordCollectionId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "TopicId", referencedColumnName = "id"))
    Set<Topic> topics;
}
