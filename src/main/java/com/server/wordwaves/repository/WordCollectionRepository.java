package com.server.wordwaves.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.vocabulary.WordCollection;

@Repository
public interface WordCollectionRepository extends JpaRepository<WordCollection, String> {}
