package com.server.wordwaves.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.server.wordwaves.dto.request.vocabulary.WordProcessUpdateRequest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordInLearningChangeEvent extends ApplicationEvent {
    List<WordProcessUpdateRequest> wordProcessUpdates;
    String currentUserId;

    public WordInLearningChangeEvent(
            Object source, List<WordProcessUpdateRequest> wordProcessUpdates, String currentUserId) {
        super(source);
        this.wordProcessUpdates = wordProcessUpdates;
        this.currentUserId = currentUserId;
    }
}
