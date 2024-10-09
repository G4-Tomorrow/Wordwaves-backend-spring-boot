package com.server.wordwaves.entity.vocabulary;

import com.server.wordwaves.entity.common.BaseAuthor;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "collection")
public class WordCollection extends BaseAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "name", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String name;
    String thumbnailName;

    @ManyToOne
    @JoinColumn(name = "collection_category_id", referencedColumnName = "id")
    WordCollectionCategory wordCollectionCategory;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "collections_topics")
    Set<Topic> topics;
}
