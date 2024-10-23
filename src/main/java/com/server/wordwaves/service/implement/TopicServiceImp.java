package com.server.wordwaves.service.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.wordwaves.dto.request.vocabulary.TopicAddWordsRequest;
import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.request.vocabulary.TopicUpdateRequest;
import com.server.wordwaves.dto.response.common.Pagination;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.common.QueryOptions;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import com.server.wordwaves.entity.vocabulary.Topic;
import com.server.wordwaves.entity.vocabulary.Word;
import com.server.wordwaves.entity.vocabulary.WordCollection;
import com.server.wordwaves.event.WordsChangedEvent;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.TopicMapper;
import com.server.wordwaves.repository.TopicRepository;
import com.server.wordwaves.repository.WordCollectionRepository;
import com.server.wordwaves.repository.WordRepository;
import com.server.wordwaves.service.TopicService;
import com.server.wordwaves.utils.MyStringUtils;
import com.server.wordwaves.utils.WordUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicServiceImp implements TopicService {
    TopicRepository topicRepository;
    WordCollectionRepository wordCollectionRepository;
    WordRepository wordRepository;
    TopicMapper topicMapper;
    ApplicationEventPublisher eventPublisher;
    WordUtils wordUtils;

    @Override
    public TopicResponse create(TopicCreationRequest request) {
        Topic topic = topicMapper.toTopic(request);
        Optional<WordCollection> wordCollectionOptional = wordCollectionRepository.findById(request.getCollectionId());

        // nếu topic ko tồn tai thì bắn ra lỗi
        if (wordCollectionOptional.isEmpty()) throw new AppException(ErrorCode.WORD_COLLECTION_NOT_EXISTED);

        //
        WordCollection wordCollection = wordCollectionOptional.get();
        Topic createdTopic = topicRepository.save(topic);
        wordCollection.getTopics().add(createdTopic);

        wordCollectionRepository.save(wordCollection);
        return topicMapper.toTopicResponse(createdTopic);
    }

    @Override
    public PaginationInfo<List<WordResponse>> getWords(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String topicId) {
        --pageNumber;
        Sort sort = MyStringUtils.isNullOrEmpty(sortBy)
                ? Sort.unsorted()
                : sortDirection.equalsIgnoreCase("DESC")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Word> wordPage;
        // Nếu có searchQuery thì tìm kiếm với phân trang, ngược lại thì tìm bình thường
        if (MyStringUtils.isNotNullAndNotEmpty(searchQuery)) {
            wordPage = topicRepository.findWordsByTopicIdAndNameContainingIgnoreCase(topicId, searchQuery, pageable);
        } else {
            wordPage = topicRepository.findWordsByTopicId(topicId, pageable);
        }

        List<WordResponse> wordResponses =
                wordPage.map(wordUtils::getWordDetail).toList();

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

    // Hàm add word vào trong topic, nếu như có 1 word ko hợp lệ thì sẽ rollback và ném ra ngoại lệ
    @Override
    @Transactional
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 3))
    public void addWords(String topicId, TopicAddWordsRequest request) {
        Topic topic =
                topicRepository.findById(topicId).orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_EXISTED));

        // Get topic và thêm words vào
        List<Word> words = request.getWordIds().stream()
                .map(wordRepository::findById)
                .map(optionalWord -> optionalWord.orElseThrow(() -> new AppException(ErrorCode.WORD_NOT_EXISTED)))
                .toList();

        topic.getWords().addAll(words);
        topicRepository.save(topic);

        // publish event để tính toán lại số lượng từ vựng của topic
        eventPublisher.publishEvent(new WordsChangedEvent(this, List.of(topicId)));
    }

    @Override
    public TopicResponse updateById(String topicId, TopicUpdateRequest request) {
        Topic topic =
                topicRepository.findById(topicId).orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_EXISTED));

        // Cập nhật name nếu không rỗng
        String updatedName = request.getName();
        if (MyStringUtils.isNotNullAndNotEmpty(updatedName)) {
            topic.setName(updatedName);
        }

        // Cập nhật thumbnailName nếu không rỗng
        String updatedThumbnailName = request.getThumbnailName();
        if (MyStringUtils.isNotNullAndNotEmpty(updatedThumbnailName)) {
            topic.setThumbnailName(updatedThumbnailName);
        }

        return topicMapper.toTopicResponse(topicRepository.save(topic));
    }

    @Override
    @Transactional
    public void deleteById(String topicId) {
        wordCollectionRepository.deleteRelationsByTopicId(topicId);

        topicRepository.deleteById(topicId);
    }

    @Override
    public PaginationInfo<List<TopicResponse>> getTopics(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String userId) {
        Sort sort = MyStringUtils.isNullOrEmpty(sortBy)
                ? Sort.unsorted()
                : sortDirection.equalsIgnoreCase("DESC")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(--pageNumber, pageSize, sort);

        Page<Topic> topicPage = MyStringUtils.isNullOrEmpty(searchQuery)
                ? (userId == null
                        ? topicRepository.findAll(pageable)
                        : topicRepository.findByCreatedById(userId, pageable))
                : (userId == null
                        ? topicRepository.findByNameContainingIgnoreCase(searchQuery, pageable)
                        : topicRepository.findByCreatedByIdAndNameContainingIgnoreCase(userId, searchQuery, pageable));

        // Chuyển đổi Topic sang TopicResponse
        List<TopicResponse> topicResponses =
                topicPage.map(topicMapper::toTopicResponse).toList();

        // Xây dựng đối tượng kết quả
        return PaginationInfo.<List<TopicResponse>>builder()
                .pagination(Pagination.builder()
                        .pageNumber(++pageNumber)
                        .pageSize(pageSize)
                        .totalPages(topicPage.getTotalPages())
                        .totalElements(topicPage.getTotalElements())
                        .build())
                .queryOptions(QueryOptions.builder()
                        .sortBy(sortBy)
                        .sortDirection(sortDirection)
                        .searchQuery(searchQuery)
                        .build())
                .data(topicResponses)
                .build();
    }
}
