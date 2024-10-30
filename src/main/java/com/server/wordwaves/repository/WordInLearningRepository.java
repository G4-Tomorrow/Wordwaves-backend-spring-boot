package com.server.wordwaves.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.vocabulary.WordInLearning;

import java.util.Collection;
import java.util.List;

@Repository
public interface WordInLearningRepository extends JpaRepository<WordInLearning, String> {
}
