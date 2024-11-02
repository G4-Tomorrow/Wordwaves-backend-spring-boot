package com.server.wordwaves.service.implement;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.server.wordwaves.constant.Level;
import com.server.wordwaves.dto.request.vocabulary.WordProcessUpdateRequest;
import com.server.wordwaves.dto.response.vocabulary.VocabularyLearningResponse;
import com.server.wordwaves.dto.response.vocabulary.WordInLearningResponse;
import com.server.wordwaves.dto.response.vocabulary.WordProcessUpdateResponse;
import com.server.wordwaves.entity.vocabulary.Word;
import com.server.wordwaves.entity.vocabulary.WordInLearning;
import com.server.wordwaves.event.WordInLearningChangeEvent;
import com.server.wordwaves.mapper.LearningMapper;
import com.server.wordwaves.repository.*;
import com.server.wordwaves.service.WordInLearningService;
import com.server.wordwaves.utils.LearningUtils;
import com.server.wordwaves.utils.RandomUtils;
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
    WordRepository wordRepository;
    UserRepository userRepository;
    LearningMapper learningMapper;
    ApplicationEventPublisher eventPublisher;

    @Override
    // 4.07
    public VocabularyLearningResponse learningWordCollection(String collectionId, int numOfWords) {
        String currentUserId = UserUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(0, numOfWords);

        // Lấy danh sách các từ chưa học
        List<Word> words = wordInLearningRepository.findAvailableWordsInTopics(collectionId, currentUserId, pageable);

        log.info("{}", words.toArray());

        // Chuyển đổi các từ thành đối tượng response
        List<WordInLearningResponse> wordResponses = words.stream()
                .map(word -> WordInLearningResponse.builder()
                        .level(Level.NOT_RETAINED)
                        .word(wordUtils.getWordDetail(word))
                        .learningType(RandomUtils.getRandomLearningType())
                        .build())
                .collect(Collectors.toList());

        // Trả về kết quả
        return VocabularyLearningResponse.builder()
                .numOfWords(numOfWords)
                .words(wordResponses)
                .build();
    }

    public List<WordProcessUpdateResponse> updateProcess(List<WordProcessUpdateRequest> words) {
        String currentUserId = UserUtils.getCurrentUserId();

        // Lấy danh sách wordId đã tồn tại trong word_in_learning của người dùng hiện tại
        Set<String> existingWordIds = new HashSet<>(wordInLearningRepository.findIdsByUserId(currentUserId));

        // Phân loại các từ đã có và chưa có
        List<WordProcessUpdateRequest> toUpdate = new ArrayList<>();
        List<WordInLearning> toInsert = new ArrayList<>();

        for (WordProcessUpdateRequest req : words) {
            if (existingWordIds.contains(req.getWordId())) {
                toUpdate.add(req);
            } else {
                int score = req.getIsCorrect() ? 1 : 0;
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
