package com.server.wordwaves.service.implement;

import com.server.wordwaves.constant.Level;
import com.server.wordwaves.dto.request.vocabulary.WordProcessUpdateRequest;
import com.server.wordwaves.dto.response.vocabulary.VocabularyLearningResponse;
import com.server.wordwaves.dto.response.vocabulary.WordInLearningResponse;
import com.server.wordwaves.entity.user.User;
import com.server.wordwaves.entity.vocabulary.Word;
import com.server.wordwaves.entity.vocabulary.WordInLearning;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordInLearningServiceImp implements WordInLearningService {
    WordInLearningRepository wordInLearningRepository;
    WordUtils wordUtils;
    WordRepository wordRepository;
    UserRepository userRepository;

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

    @Override
    public List<WordProcessUpdateRequest> updateProcess(List<WordProcessUpdateRequest> words) {
        String currentUserId = UserUtils.getCurrentUserId();
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy danh sách wordId đã tồn tại trong word_in_learning của người dùng hiện tại
        List<String> existingWordIds = wordInLearningRepository.findIdsByUserId(currentUserId);

        // Phân loại các từ đã có và chưa có
        List<WordProcessUpdateRequest> toUpdate = new ArrayList<>();
        List<WordProcessUpdateRequest> toInsert = new ArrayList<>();

        for (WordProcessUpdateRequest req : words) {
            if (existingWordIds.contains(req.getWordId())) {
                toUpdate.add(req);
            } else {
                toInsert.add(req);
            }
        }

        // Cập nhật score cho các từ đã tồn tại
        for (WordProcessUpdateRequest req : toUpdate) {
            wordInLearningRepository.updateScore(
                    UUID.fromString(req.getWordId()),
                    req.isCorrect() ? 1 : -1
            );
        }

        // Chèn mới các từ chưa có vào word_in_learning
        for (WordProcessUpdateRequest req : toInsert) {
            int score = req.isCorrect() ? 1 : 0;
            WordInLearning newWord = WordInLearning.builder()
                    .word(wordRepository.findById(req.getWordId()).orElseThrow())
                    .score(score)
                    .nextReviewTiming(LearningUtils.calculateNextPreviewTiming(score))
                    .user(user)
                    .build();
            wordInLearningRepository.save(newWord);
        }

        return words;
    }

}
