package com.server.wordwaves.repository;

import java.util.List;
import java.util.UUID;

import com.server.wordwaves.entity.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.vocabulary.Word;
import com.server.wordwaves.entity.vocabulary.WordInLearning;

@Repository
public interface WordInLearningRepository extends JpaRepository<WordInLearning, String> {

    @Query(name = "Word.findAvailableWordsInTopics", nativeQuery = true)
    List<Word> findAvailableWordsInTopics(
            @Param("collectionId") String collectionId,
            @Param("currentUserId") String currentUserId,
            Pageable pageable);

    @Query(value = "SELECT wil.WordId FROM WordInLearning wil WHERE UserId = :currentUserId", nativeQuery = true)
    List<String> findIdsByUserId(String currentUserId);

    WordInLearning findByUserIdAndWordId(String userId, String wordId);
}
