package com.server.wordwaves.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.vocabulary.WordCollectionCategory;

@Repository
public interface WordCollectionCategoryRepository extends JpaRepository<WordCollectionCategory, String> {
    Optional<WordCollectionCategory> findByName(String category);
}
