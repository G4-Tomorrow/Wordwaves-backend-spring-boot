package com.server.wordwaves.repository;

import com.server.wordwaves.entity.vocabulary.WordInLearning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordInLearningRepository extends JpaRepository<WordInLearning, String> {
}
