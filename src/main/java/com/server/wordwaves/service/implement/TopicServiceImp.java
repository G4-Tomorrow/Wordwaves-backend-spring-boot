package com.server.wordwaves.service.implement;

import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.response.common.Pagination;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.common.QueryOptions;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import com.server.wordwaves.entity.vocabulary.Topic;
import com.server.wordwaves.entity.vocabulary.Word;
import com.server.wordwaves.entity.vocabulary.WordCollection;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.TopicMapper;
import com.server.wordwaves.mapper.WordMapper;
import com.server.wordwaves.repository.TopicRepository;
import com.server.wordwaves.repository.WordCollectionRepository;
import com.server.wordwaves.repository.httpclient.DictionaryClient;
import com.server.wordwaves.service.TopicService;
import com.server.wordwaves.utils.MyStringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicServiceImp implements TopicService {
    TopicRepository topicRepository;
    WordCollectionRepository wordCollectionRepository;
    TopicMapper topicMapper;
    DictionaryClient dictionaryClient;
    WordMapper wordMapper;

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

        List<WordResponse> wordResponses = wordPage.map(word -> {
            List<WordResponse> tmp = dictionaryClient.retrieveEntries(word.getName());
            WordResponse wordResponse = tmp.getFirst();
            wordResponse.setId(word.getId());
            wordResponse.setName(word.getName());
            wordResponse.setThumbnailUrl(word.getThumbnailUrl());
            wordResponse.setCreatedAt(word.getCreatedAt());
            wordResponse.setUpdatedAt(word.getUpdatedAt());
            wordResponse.setCreatedById(word.getCreatedById());
            wordResponse.setVietnamese(word.getVietnamese());
            return wordResponse;
        }).toList();

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
