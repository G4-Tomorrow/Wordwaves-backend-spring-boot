package com.server.wordwaves.service.implement;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.server.wordwaves.constant.Level;
import com.server.wordwaves.dto.response.vocabulary.WordInLearningResponse;
import com.server.wordwaves.entity.vocabulary.Topic;
import com.server.wordwaves.entity.vocabulary.Word;
import com.server.wordwaves.repository.TopicRepository;
import com.server.wordwaves.utils.RandomUtils;
import com.server.wordwaves.utils.WordUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.server.wordwaves.dto.response.vocabulary.VocabularyLearningResponse;
import com.server.wordwaves.entity.vocabulary.WordCollection;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.repository.WordCollectionRepository;
import com.server.wordwaves.repository.WordInLearningRepository;
import com.server.wordwaves.repository.WordRepository;
import com.server.wordwaves.service.WordInLearningService;
import com.server.wordwaves.utils.UserUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordInLearningServiceImp implements WordInLearningService {
    private final TopicRepository topicRepository;
    WordInLearningRepository wordInLearningRepository;
    WordRepository wordRepository;
    WordUtils wordUtils;
    WordCollectionRepository wordCollectionRepository;

    @Override
    //4.07
    public VocabularyLearningResponse learningWordCollection(String collectionId, int numOfWords) {
        String currentUserId = UserUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(0, numOfWords);


        // Lấy danh sách các từ chưa học
        List<Object> words = wordInLearningRepository.findAvailableWordsInTopics(collectionId, currentUserId, pageable);

        log.info("{}",words.toArray());

        // Chuyển đổi các từ thành đối tượng response
//        List<WordInLearningResponse> wordResponses = words.stream()
//                .map(word -> WordInLearningResponse.builder()
//                        .level(Level.NOT_RETAINED)
//                        .word(wordUtils.getWordDetail(word))
//                        .learningType(RandomUtils.getRandomLearningType())
//                        .build()
//                ).collect(Collectors.toList());

        // Trả về kết quả
        return VocabularyLearningResponse.builder()
                .numOfWords(numOfWords)
//                .words(wordResponses)
                .build();
    }

}
