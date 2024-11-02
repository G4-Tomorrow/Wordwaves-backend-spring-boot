package com.server.wordwaves.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.vocabulary.Word;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, String> {
    Page<Word> findByCreatedByIdAndNameContainingIgnoreCase(String createdById, String name, Pageable pageable);

    Page<Word> findByCreatedById(String createdById, Pageable pageable);

    Page<Word> findWordsWithoutTopicsByCreatedByIdAndNameContaining(String createdById, String name, Pageable pageable);

    Page<Word> findWordsWithoutTopicsByCreatedById(String userId, Pageable pageable);

    @Query("SELECT w.createdById FROM Word w WHERE w.id = :identifierId")
    String findCreatedByIdById(@Param("identifierId") String identifierId);

//    @Query("SELECT new com.server.wordwaves.entity.vocabulary.Word(w.id, w.name, w.vietnamese, w.thumbnailUrl) FROM Word w WHERE w.name = :name")
    Optional<Word> findByName(String name);
}
