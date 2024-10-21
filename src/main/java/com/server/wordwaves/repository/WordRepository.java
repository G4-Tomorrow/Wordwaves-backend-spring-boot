package com.server.wordwaves.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.vocabulary.Word;

@Repository
public interface WordRepository extends JpaRepository<Word, String> {
    Page<Word> findByNameContainingIgnoreCase(String searchQuery, Pageable pageable);

    @Query(value = "SELECT w.* FROM Word w WHERE w.Id NOT IN (SELECT tw.WordId FROM TopicToWord tw) AND w.Name LIKE %:searchQuery%", nativeQuery = true)
    Page<Word> findWordsWithoutTopicsByNameContaining(@Param("searchQuery") String searchQuery, Pageable pageable);

}
