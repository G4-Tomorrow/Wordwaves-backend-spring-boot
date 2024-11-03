package com.server.wordwaves.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.vocabulary.WordInLearning;

@Repository
public interface WordInLearningRepository extends JpaRepository<WordInLearning, String> {

    @Query(
            value =
                    """
			SELECT w.Name, w.Id
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
			""",
            nativeQuery = true)
    List<Object[]> findAvailableWordsInCollection(
            @Param("collectionId") String collectionId,
            @Param("currentUserId") String currentUserId,
            Pageable pageable);

    @Query(
            value =
                    """
		SELECT w.Name, w.Id
		FROM Word w
		JOIN TopicToWord tt ON w.id = tt.wordId
		WHERE tt.topicId = :topicId
		AND NOT EXISTS (
			SELECT 1
			FROM WordInLearning wil
			WHERE wil.wordId = w.id AND wil.userId = :currentUserId
		)
		""",
            nativeQuery = true)
    List<Object[]> findNotRetainedWordInTopic(
            @Param("topicId") String topicId, @Param("currentUserId") String currentUserId, Pageable pageable);

    @Query(
            value =
                    """
				SELECT w.Name, w.Id
				FROM Word w
				JOIN TopicToWord tt ON w.id = tt.wordId
				WHERE tt.topicId IN (
					SELECT wctt.topicId
					FROM WordCollection wc
					JOIN WordCollectionToTopic wctt ON wc.id = wctt.wordCollectionId
					WHERE wc.id = :collectionId AND wc.createdById = :currentUserId
				)
				AND EXISTS (
					SELECT 1
					FROM WordInLearning wil
					WHERE wil.wordId = w.id AND wil.userId = :currentUserId
					AND wil.nextReviewTiming < CURRENT_TIMESTAMP
				)
			""",
            nativeQuery = true)
    List<Object[]> findWordsInCollectionWithNextReviewBeforeNow(
            @Param("collectionId") String collectionId,
            @Param("currentUserId") String currentUserId,
            Pageable pageable);

    @Query(
            value =
                    """
		SELECT w.Name, w.Id
		FROM Word w
		JOIN TopicToWord tt ON w.id = tt.wordId
		WHERE tt.topicId = :topicId
		AND EXISTS (
			SELECT 1
			FROM WordInLearning wil
			WHERE wil.wordId = w.id AND wil.userId = :currentUserId
			AND wil.nextReviewTiming < CURRENT_TIMESTAMP
		)
		""",
            nativeQuery = true)
    List<Object[]> findWordsInTopicWithNextReviewBeforeNow(String topicId, String currentUserId, Pageable pageable);

	@Query(
			value =
					"""
                    SELECT w.id
                    FROM Word w
                    JOIN WordInLearning wil ON w.id = wil.wordId
                    WHERE wil.userId = :currentUserId
                    """)
	List<String> findWordNamesByUserId(@Param("currentUserId") String currentUserId);

    @Query(
            value =
                    """
			SELECT wil 
			FROM WordInLearning wil 
			JOIN Word w ON wil.wordId = w.id 
			WHERE w.id = :wordId 
			AND wil.userId = :userId
			""")
    WordInLearning findByUserIdAndWordId(@Param("userId") String userId, @Param("wordId") String wordId);
}
