package com.server.wordwaves.repository;

import com.server.wordwaves.entity.vocabulary.Word;
import jakarta.persistence.EntityResult;
import jakarta.persistence.NamedNativeQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.vocabulary.WordInLearning;

import java.util.List;

@Repository
public interface WordInLearningRepository extends JpaRepository<WordInLearning, String> {

    @Query(value = """
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
            SELECT wil.CreatedById
            FROM WordInLearning wil
            WHERE wil.CreatedById = :currentUserId
        )
        """, nativeQuery = true)
    List<Object> findAvailableWordsInTopics(@Param("collectionId") String collectionId,
                                          @Param("currentUserId") String currentUserId,
                                          Pageable pageable);
}
