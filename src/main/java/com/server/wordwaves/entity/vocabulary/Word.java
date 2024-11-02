package com.server.wordwaves.entity.vocabulary;

import com.server.wordwaves.entity.common.BaseAuthor;
import jakarta.persistence.*;
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
@SqlResultSetMapping(
        name = "WordMapping",
        entities =
        @EntityResult(
                entityClass = Word.class,
                fields = {
                        @FieldResult(name = "id", column = "Id"),
                        @FieldResult(name = "name", column = "Name"),
                        @FieldResult(name = "vietnamese", column = "Vietnamese"),
                        @FieldResult(name = "thumbnailUrl", column = "ThumbnailUrl"),
                        @FieldResult(name = "createdAt", column = "CreatedAt"),
                        @FieldResult(name = "updatedAt", column = "UpdatedAt"),
                        @FieldResult(name = "createdById", column = "CreatedById"),
                        @FieldResult(name = "updatedById", column = "UpdatedById"),
                }))
@NamedNativeQuery(
        name = "Word.findAvailableWordsInTopics",
        query =
                """
                        SELECT w.*
                        FROM Word w                        
                        JOIN TopicToWord tt ON w.Id = tt.WordId
                        WHERE tt.TopicId IN (
                            SELECT wctt.TopicId
                            FROM WordCollection wc
                            RIGHT JOIN WordCollectionToTopic wctt ON wc.Id = wctt.WordCollectionId
                            WHERE wc.Id = :collectionId
                        )
                        AND w.Id NOT IN (
                            SELECT wil.UserId
                            FROM WordInLearning wil
                            WHERE wil.UserId = :currentUserId
                        )
                        """,
        resultSetMapping = "WordMapping")
public class Word extends BaseAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String name;

    String vietnamese;

    String thumbnailUrl;
}
