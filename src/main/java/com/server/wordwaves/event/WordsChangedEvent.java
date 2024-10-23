package com.server.wordwaves.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordsChangedEvent extends ApplicationEvent {
    List<String> topicIds;

    public WordsChangedEvent(Object source, List<String> topicIds) {
        super(source);
        this.topicIds = topicIds;
    }
}
