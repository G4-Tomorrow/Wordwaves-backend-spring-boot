package com.server.wordwaves.event.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.server.wordwaves.event.WordsChangedEvent;
import com.server.wordwaves.repository.TopicRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordsChangedListener implements ApplicationListener<WordsChangedEvent> {

    TopicRepository topicRepository;

    @Override
    //    @Transactional
    //    @Retryable(
    //            value = {ObjectOptimisticLockingFailureException.class},
    //            maxAttempts = 3,
    //            backoff = @Backoff(delay = 500, multiplier = 3)
    //    )
    public void onApplicationEvent(WordsChangedEvent event) {
        var topicIds = event.getTopicIds();

        topicIds.forEach(topicId -> {
            topicRepository.findById(topicId).ifPresent(topic -> {
                // Cập nhật số lượng từ trong topic
                int totalWords = topic.getWords().size();
                topic.setNumOfTotalWords(totalWords);
                topicRepository.save(topic); // Lưu lại thay đổi
            });
        });
    }
}
