package com.server.wordwaves.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.vocabulary.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String> {}
