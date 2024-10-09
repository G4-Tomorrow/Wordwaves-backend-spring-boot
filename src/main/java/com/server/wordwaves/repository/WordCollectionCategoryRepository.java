package com.server.wordwaves.repository;

import com.server.wordwaves.entity.vocabulary.WordCollectionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordCollectionCategoryRepository extends JpaRepository<WordCollectionCategory, String> {
    Optional<WordCollectionCategory> findByName(String category);
}
