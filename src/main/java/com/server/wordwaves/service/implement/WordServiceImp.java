package com.server.wordwaves.service.implement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
import com.server.wordwaves.dto.response.common.Pagination;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.common.QueryOptions;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import com.server.wordwaves.dto.response.vocabulary.WordThumbnailResponse;
import com.server.wordwaves.entity.vocabulary.Topic;
import com.server.wordwaves.entity.vocabulary.Word;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.WordMapper;
import com.server.wordwaves.repository.TopicRepository;
import com.server.wordwaves.repository.WordRepository;
import com.server.wordwaves.repository.httpclient.DictionaryClient;
import com.server.wordwaves.repository.httpclient.ImageClient;
import com.server.wordwaves.service.WordService;
import com.server.wordwaves.utils.MyStringUtils;

import feign.FeignException;
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
    DictionaryClient dictionaryClient;
    ImageClient imageClient;

    ObjectMapper objectMapper;

    @NonFinal
    @Value("${app.pexels-client.apikey}")
    String pexelsApiKey;

    @Override
    @Transactional
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3, // Thử retry tối đa 3 lần
            backoff = @Backoff(delay = 500, multiplier = 1.5) // Đợi 500ms trước lần retry kế tiếp
            )
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
            topic.incrementTotalWords(1);
            topicRepository.save(topic);
        }

        // Lấy từ điển
        List<WordResponse> wordResponses = null;
        try {
            wordResponses = dictionaryClient.retrieveEntries(word.getName());
        } catch (FeignException e) {
            log.info("!!!ERROR: Lấy thông tin từ vựng thất bại");
        }

        // Lấy từ phản hồi đầu tiên từ danh sách
        WordResponse wordResponse;
        if (!wordResponses.isEmpty()) {
            wordResponse = wordResponses.get(0);
            wordResponse.setId(createdWord.getId());
            wordResponse.setName(createdWord.getName());
            wordResponse.setThumbnailUrl(thumbnailUrl);
            wordResponse.setCreatedAt(createdWord.getCreatedAt());
            wordResponse.setUpdatedAt(createdWord.getUpdatedAt());
            wordResponse.setCreatedById(createdWord.getCreatedById());
            wordResponse.setUpdatedById(createdWord.getUpdatedById());
        } else {
            // Nếu từ vựng hợp lệ nhưng chưa có định nghĩa
            wordResponse = wordMapper.toWordResponse(createdWord);
        }
        return wordResponse;
    }

    @Override
    public PaginationInfo<List<WordResponse>> getWords(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery) {
        --pageNumber;
        Sort sort = MyStringUtils.isNullOrEmpty(sortBy)
                ? Sort.unsorted()
                : sortDirection.equalsIgnoreCase("DESC")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Word> wordPage = MyStringUtils.isNullOrEmpty(searchQuery)
                ? wordRepository.findAll(pageable)
                : wordRepository.findByNameContainingIgnoreCase(searchQuery, pageable);

        List<WordResponse> wordResponses = wordPage.map(word -> {
                    try {
                        List<WordResponse> tmp = dictionaryClient.retrieveEntries(word.getName());
                        WordResponse wordResponse = tmp.getFirst();
                        wordResponse.setId(word.getId());
                        wordResponse.setName(word.getName());
                        wordResponse.setThumbnailUrl(word.getThumbnailUrl());
                        wordResponse.setCreatedAt(word.getCreatedAt());
                        wordResponse.setUpdatedAt(word.getUpdatedAt());
                        wordResponse.setCreatedById(word.getCreatedById());
                        wordResponse.setUpdatedById(word.getUpdatedById());
                        wordResponse.setVietnamese(word.getVietnamese());
                        return wordResponse;
                    } catch (FeignException e) {
                        return wordMapper.toWordResponse(word);
                    }
                })
                .toList();

        return PaginationInfo.<List<WordResponse>>builder()
                .pagination(Pagination.builder()
                        .pageNumber(++pageNumber)
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
}
