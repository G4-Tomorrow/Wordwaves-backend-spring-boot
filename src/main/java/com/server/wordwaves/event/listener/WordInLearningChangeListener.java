package com.server.wordwaves.event.listener;

import com.server.wordwaves.constant.Level;
import com.server.wordwaves.dto.request.vocabulary.WordProcessUpdateRequest;
import com.server.wordwaves.entity.vocabulary.WordInLearning;
import com.server.wordwaves.event.WordInLearningChangeEvent;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.repository.WordInLearningRepository;
import com.server.wordwaves.utils.LearningUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordInLearningChangeListener implements ApplicationListener<WordInLearningChangeEvent> {
    WordInLearningRepository wordInLearningRepository;

    @Override
    @Async
    public void onApplicationEvent(WordInLearningChangeEvent event) {
        List<WordProcessUpdateRequest> updates = event.getWordProcessUpdates();
        String userId = event.getCurrentUserId();

        for (WordProcessUpdateRequest update : updates) {
            WordInLearning wordInLearning = wordInLearningRepository.findByUserIdAndWordId(userId, update.getWordId());

            if (Objects.isNull(wordInLearning)) {
                throw new AppException(ErrorCode.WORD_IN_LEARNING_NOT_EXISTED);
            }

            // Update score
            int scoreChange = update.getIsCorrect() ? 1 : -1;
            int newScore = wordInLearning.getScore() + scoreChange;
            wordInLearning.setScore(newScore);

            // Update level and nextReviewTiming
            Level currentLevel = LearningUtils.getCurrentLevel(newScore);
            Instant nextReviewTiming = LearningUtils.calculateNextPreviewTiming(currentLevel.getScore());
            wordInLearning.setNextReviewTiming(nextReviewTiming);
            wordInLearning.setLevel(currentLevel);

            // Save changes
            wordInLearningRepository.save(wordInLearning);
        }
    }
}

