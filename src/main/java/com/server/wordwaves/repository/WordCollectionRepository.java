package com.server.wordwaves.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.vocabulary.Topic;
import com.server.wordwaves.entity.vocabulary.WordCollection;

@Repository
public interface WordCollectionRepository
        extends JpaRepository<WordCollection, String>, JpaSpecificationExecutor<WordCollection> {

    @Query(
            "SELECT t FROM WordCollection wc JOIN wc.topics t WHERE wc.id = :collectionId AND t.name LIKE %:searchQuery%")
    Page<Topic> findTopicsByIdAndNameContainingIgnoreCase(
            @Param("collectionId") String collectionId, @Param("searchQuery") String searchQuery, Pageable pageable);

    // Lấy toàn bộ các topic theo collectionId
    @Query("SELECT t FROM WordCollection wc JOIN wc.topics t WHERE wc.id = :collectionId")
    Page<Topic> findTopicsById(@Param("collectionId") String collectionId, Pageable pageable);
}
