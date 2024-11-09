package com.server.wordwaves.service.implement;

import java.util.*;

import com.server.wordwaves.dto.model.vocabulary.WordInLearningModel;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.server.wordwaves.constant.Level;
import com.server.wordwaves.dto.request.vocabulary.WordProcessUpdateRequest;
import com.server.wordwaves.dto.response.vocabulary.VocabularyLearningResponse;
import com.server.wordwaves.dto.response.vocabulary.VocabularyRevisionResponse;
import com.server.wordwaves.dto.response.vocabulary.WordProcessUpdateResponse;
import com.server.wordwaves.entity.vocabulary.WordInLearning;
import com.server.wordwaves.event.WordInLearningChangeEvent;
import com.server.wordwaves.mapper.LearningMapper;
import com.server.wordwaves.repository.*;
import com.server.wordwaves.service.WordInLearningService;
import com.server.wordwaves.utils.LearningUtils;
import com.server.wordwaves.utils.UserUtils;
import com.server.wordwaves.utils.WordUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordInLearningServiceImp implements WordInLearningService {
    WordInLearningRepository wordInLearningRepository;
    WordUtils wordUtils;
    LearningMapper learningMapper;
    ApplicationEventPublisher eventPublisher;

    @Override
    public VocabularyLearningResponse learningWordCollection(String collectionId, int numOfWords) {
        String currentUserId = UserUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(0, numOfWords);

        // Lấy danh sách các từ chưa học
        List<Object[]> wordInforms =
                wordInLearningRepository.findAvailableWordsInCollection(collectionId, currentUserId, pageable);

        // Trả về kết quả
        return VocabularyLearningResponse.builder()
                .numOfWords(numOfWords)
                .numOfNotRetainedWords(wordInforms.size())
                .words(LearningUtils.toWordInLearningResponses(wordInforms))
                .build();
    }

    @Override
    public VocabularyLearningResponse learningTopic(String topicId, int numOfWords) {
        String currentUserId = UserUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(0, numOfWords);

        List<Object[]> wordInforms = wordInLearningRepository.findNotRetainedWordInTopic(topicId, currentUserId, pageable);

        return VocabularyLearningResponse.builder()
                .numOfWords(numOfWords)
                .numOfNotRetainedWords(wordInforms.size())
                .words(LearningUtils.toWordInLearningResponses(wordInforms))
                .build();
    }

    @Override
    public VocabularyRevisionResponse reviewWordCollection(String collectionId, int numOfWords) {
        String currentUserId = UserUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(0, numOfWords);

        List<Object[]> wordInforms = wordInLearningRepository.findWordsInCollectionWithNextReviewBeforeNow(
                collectionId, currentUserId, pageable);

        return VocabularyRevisionResponse.builder()
                .numOfWords(numOfWords)
                .numOfReviewedWords(wordInforms.size())
                .words(LearningUtils.toWordInLearningResponses(wordInforms))
                .build();
    }

    @Override
    public VocabularyRevisionResponse reviewTopic(String topicId, int numOfWords) {
        String currentUserId = UserUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(0, numOfWords);

        List<Object[]> wordInforms =
                wordInLearningRepository.findWordsInTopicWithNextReviewBeforeNow(topicId, currentUserId, pageable);

        return VocabularyRevisionResponse.builder()
                .numOfWords(numOfWords)
                .numOfReviewedWords(wordInforms.size())
                .words(LearningUtils.toWordInLearningResponses(wordInforms))
                .build();
    }

    public List<WordProcessUpdateResponse> updateProcess(List<WordProcessUpdateRequest> words) {
        String currentUserId = UserUtils.getCurrentUserId();

        // Lấy danh sách wordId đã tồn tại trong word_in_learning của người dùng hiện tại
        Set<String> existingWordIds = new HashSet<>(wordInLearningRepository.findWordNamesByUserId(currentUserId));

        // Phân loại các từ đã có và chưa có
        List<WordProcessUpdateRequest> toUpdate = new ArrayList<>();
        List<WordInLearning> toInsert = new ArrayList<>();

        for (WordProcessUpdateRequest req : words) {
            if (existingWordIds.contains(req.getWordId())) {
                toUpdate.add(req);
            } else {
                int score = req.getIsCorrect() ? 1 : 0;
                score = req.getIsAlreadyKnow() ? 6 : score;
                WordInLearning newWord = WordInLearning.builder()
                        .wordId(req.getWordId())
                        .score(score)
                        .level(Level.values()[score])
                        .nextReviewTiming(LearningUtils.calculateNextPreviewTiming(score))
                        .userId(currentUserId)
                        .build();
                toInsert.add(newWord);
            }
        }

        // Publish the event for updates
        eventPublisher.publishEvent(new WordInLearningChangeEvent(this, toUpdate, currentUserId));

        // Batch save new words
        if (!toInsert.isEmpty()) {
            wordInLearningRepository.saveAll(toInsert);
        }

        return words.stream().map(learningMapper::toWordProcessUpdateResponse).toList();
    }
}
