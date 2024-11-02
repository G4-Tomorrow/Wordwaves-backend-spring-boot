package com.server.wordwaves.repository;

import com.server.wordwaves.entity.vocabulary.WordInLearning;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordInLearningRepository extends JpaRepository<WordInLearning, String> {

    @Query(value = """
              SELECT w.Id
                                                             FROM Word w
                                                             JOIN TopicToWord tt ON w.Id = tt.WordId
                                                             WHERE tt.TopicId IN (
                                                                 SELECT wctt.TopicId
                                                                 FROM WordCollection wc
                                                                 JOIN WordCollectionToTopic wctt ON wc.Id = wctt.WordCollectionId
                                                                 WHERE wc.Id = :collectionId
                                                             )
                                                             AND NOT EXISTS (
                                                                 SELECT 1
                                                                 FROM WordInLearning wil
                                                                 WHERE wil.UserId = :currentUserId AND wil.WordId = w.Id
                                                             )
            """, nativeQuery = true)
    List<String> findAvailableWordsInTopics(
            @Param("collectionId") String collectionId,
            @Param("currentUserId") String currentUserId,
            Pageable pageable);

    @Query(value = "SELECT wil.WordId FROM WordInLearning wil WHERE UserId = :currentUserId", nativeQuery = true)
    List<String> findIdsByUserId(String currentUserId);

    WordInLearning findByUserIdAndWordId(String userId, String wordId);
}
