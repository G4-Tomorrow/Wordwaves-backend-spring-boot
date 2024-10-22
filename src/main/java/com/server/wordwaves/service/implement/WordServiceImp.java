package com.server.wordwaves.service.implement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
import com.server.wordwaves.dto.request.vocabulary.WordUpdateRequest;
import com.server.wordwaves.dto.response.common.Pagination;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.common.QueryOptions;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import com.server.wordwaves.dto.response.vocabulary.WordThumbnailResponse;
import com.server.wordwaves.entity.vocabulary.Topic;
import com.server.wordwaves.entity.vocabulary.Word;
import com.server.wordwaves.event.WordsChangedEvent;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.WordMapper;
import com.server.wordwaves.repository.TopicRepository;
import com.server.wordwaves.repository.WordRepository;
import com.server.wordwaves.repository.httpclient.ImageClient;
import com.server.wordwaves.service.WordService;
import com.server.wordwaves.utils.MyStringUtils;
import com.server.wordwaves.utils.WordUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordServiceImp implements WordService {
    WordRepository wordRepository;
    TopicRepository topicRepository;
    WordMapper wordMapper;
    ImageClient imageClient;
    ApplicationEventPublisher eventPublisher;
    WordUtils wordUtils;

    @NonFinal
    @Value("${app.pexels-client.apikey}")
    String pexelsApiKey;

    @Override
    @Transactional
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 3))
    public WordResponse create(WordCreationRequest request) {
        Word word = wordMapper.toWord(request);

        // Lấy ảnh và set vào word
        WordThumbnailResponse wordThumbnailResponse =
                imageClient.retrieveWordThumbnailUrl(pexelsApiKey, word.getName(), 1);
        String thumbnailUrl;

        try {
            thumbnailUrl = wordThumbnailResponse.getPhotos().getFirst().getSrc().getOriginal();
        } catch (NullPointerException | NoSuchElementException e) {
            throw new AppException(ErrorCode.WORD_NOT_EXISTED);
        }
        word.setThumbnailUrl(thumbnailUrl);

        // Nếu có topic thì sẽ set word vào topic đó
        String topicId = request.getTopicId();
        Optional<Topic> topicOptional = topicRepository.findById(topicId);

        Word createdWord = null;
        try {
            createdWord = wordRepository.saveAndFlush(word);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.WORD_EXISTED);
        }

        // set word vào topic nếu topic tồn tại
        if (topicOptional.isPresent()) {
            Topic topic = topicOptional.get();
            topic.getWords().add(createdWord);
            topicRepository.save(topic);
            eventPublisher.publishEvent(new WordsChangedEvent(this, List.of(topicId)));
        }

        // Lấy từ điển
        return wordUtils.getWordDetail(createdWord);
    }

    @Override
    public PaginationInfo<List<WordResponse>> getWords(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection,
            String searchQuery,
            String isUnassigned) {
        Sort sort = MyStringUtils.isNullOrEmpty(sortBy)
                ? Sort.unsorted()
                : sortDirection.equalsIgnoreCase("DESC")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(--pageNumber, pageSize, sort);
        Page<Word> wordPage;

        if (isUnassigned != null) {
            // Lấy danh sách từ vựng chưa thuộc về bất kỳ topic nào
            wordPage = wordRepository.findWordsWithoutTopicsByNameContaining(searchQuery, pageable);
        } else {
            // Nếu không tìm từ vựng theo các điều kiện khác
            wordPage = MyStringUtils.isNullOrEmpty(searchQuery)
                    ? wordRepository.findAll(pageable)
                    : wordRepository.findByNameContainingIgnoreCase(searchQuery, pageable);
        }

        List<WordResponse> wordResponses =
                wordPage.map(wordUtils::getWordDetail).toList();

        return PaginationInfo.<List<WordResponse>>builder()
                .pagination(Pagination.builder()
                        .pageNumber(pageNumber)
                        .pageSize(pageSize)
                        .totalPages(wordPage.getTotalPages())
                        .totalElements(wordPage.getTotalElements())
                        .build())
                .queryOptions(QueryOptions.builder()
                        .sortBy(sortBy)
                        .sortDirection(sortDirection)
                        .searchQuery(searchQuery)
                        .build())
                .data(wordResponses)
                .build();
    }

    @Override
    public void deleteById(String wordId) {
        Word word = wordRepository.findById(wordId).orElseThrow(() -> new AppException(ErrorCode.WORD_NOT_EXISTED));

        List<String> topicIds = topicRepository.findTopicIdsByWordId(wordId);

        // Cập nhật các topic để loại bỏ từ này
        for (String topicId : topicIds) {
            Topic topic = topicRepository
                    .findById(topicId)
                    .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_EXISTED)); // Xử lý nếu topic không tồn tại

            topic.getWords().remove(word);
            topicRepository.save(topic);
        }

        // Cuối cùng xóa từ
        wordRepository.delete(word);
        eventPublisher.publishEvent(new WordsChangedEvent(this, topicIds));
    }

    @Override
    public WordResponse updateById(String wordId, WordUpdateRequest request) {
        Word word = wordRepository.findById(wordId).orElseThrow(() -> new AppException(ErrorCode.WORD_NOT_EXISTED));
        String name = request.getName();
        String thumbnailUrl = request.getThumbnailUrl();

        if (MyStringUtils.isNotNullAndNotEmpty(name)) word.setName(name);
        if (MyStringUtils.isNotNullAndNotEmpty(thumbnailUrl)) word.setName(thumbnailUrl);

        return wordUtils.getWordDetail(wordRepository.save(word));
    }
}
