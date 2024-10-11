package com.server.wordwaves.service.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.server.wordwaves.dto.request.vocabulary.WordCollectionCreationRequest;
import com.server.wordwaves.dto.response.common.Pagination;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.common.QueryOptions;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordCollectionResponse;
import com.server.wordwaves.entity.vocabulary.Topic;
import com.server.wordwaves.entity.vocabulary.WordCollection;
import com.server.wordwaves.entity.vocabulary.WordCollectionCategory;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.TopicMapper;
import com.server.wordwaves.mapper.WordCollectionMapper;
import com.server.wordwaves.repository.TopicRepository;
import com.server.wordwaves.repository.UserRepository;
import com.server.wordwaves.repository.WordCollectionCategoryRepository;
import com.server.wordwaves.repository.WordCollectionRepository;
import com.server.wordwaves.service.WordCollectionService;
import com.server.wordwaves.utils.MyStringUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordCollectionServiceImp implements WordCollectionService {
    private final TopicMapper topicMapper;
    WordCollectionRepository wordCollectionRepository;
    WordCollectionMapper wordCollectionMapper;
    WordCollectionCategoryRepository wordCollectionCategoryRepository;
    TopicRepository topicRepository;
    UserRepository userRepository;

    @Override
    public WordCollectionResponse create(WordCollectionCreationRequest request) {
        String category = request.getCategory();
        WordCollection wordCollection = wordCollectionMapper.toWordCollection(request);

        // check xem category có null hay ko
        if (category != null && !category.isEmpty()) {
            Optional<WordCollectionCategory> wordCollectionCategoryOptional =
                    wordCollectionCategoryRepository.findByName(category);

            if (wordCollectionCategoryOptional.isPresent()) {
                // xử lí nếu category đã tồn tại
                wordCollection.setWordCollectionCategory(wordCollectionCategoryOptional.get());
            } else {
                // xử lí nếu category chưa tồn tại
                WordCollectionCategory wordCollectionCategory = wordCollectionCategoryRepository.save(
                        WordCollectionCategory.builder().name(category).build());
                wordCollection.setWordCollectionCategory(wordCollectionCategory);
            }
        }

        WordCollection createdWordCollection;
        try {
            createdWordCollection = wordCollectionRepository.save(wordCollection);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.COLLECTION_EXISTED);
        }
        return wordCollectionMapper.toWordCollectionResponse(createdWordCollection);
    }

    @Override
    public PaginationInfo<List<WordCollectionResponse>> getCollections(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String userId) {
        pageNumber--;

        Sort sort = MyStringUtils.isNullOrEmpty(sortBy)
                ? Sort.unsorted()
                : sortDirection.equalsIgnoreCase("DESC")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Specification<WordCollection> spec = Specification.where(null);

        // Nếu có searchQuery, thêm điều kiện tìm kiếm
        if (MyStringUtils.isNotNullAndNotEmpty(searchQuery)) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), "%" + searchQuery.toLowerCase() + "%"));
        }

        // Kiểm tra userId
        if (MyStringUtils.isNotNullAndNotEmpty(userId)) {
            // Nếu có userId, tìm kiếm theo userId
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("createdById"), userId));
        } else {
            // Nếu không có userId, lấy tất cả userId có vai trò ADMIN
            List<String> adminUserIds = userRepository.findAllUserIdsByRole("ADMIN");
            // thêm điều kiện để trả về các bản ghi được tạo bởi userid
            spec = spec.and(
                    (root, query, criteriaBuilder) -> root.get("createdById").in(adminUserIds));
        }

        // gọi database
        Page<WordCollection> wordCollectionsPage = wordCollectionRepository.findAll(spec, pageable);
        log.info("collection: {}", wordCollectionsPage);

        List<WordCollectionResponse> responses = wordCollectionsPage.getContent().stream()
                .map(wordCollectionMapper::toWordCollectionResponse)
                .toList();

        return PaginationInfo.<List<WordCollectionResponse>>builder()
                .pagination(Pagination.builder()
                        .pageNumber(++pageNumber)
                        .pageSize(pageSize)
                        .totalPages(wordCollectionsPage.getTotalPages())
                        .totalElements(wordCollectionsPage.getTotalElements())
                        .build())
                .queryOptions(QueryOptions.builder()
                        .sortBy(sortBy)
                        .sortDirection(sortDirection)
                        .searchQuery(searchQuery)
                        .build())
                .data(responses)
                .build();
    }

    @Override
    public PaginationInfo<List<TopicResponse>> getTopics(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection,
            String searchQuery,
            String collectionId) {
        --pageNumber;
        Sort sort = MyStringUtils.isNullOrEmpty(sortBy)
                ? Sort.unsorted()
                : sortDirection.equalsIgnoreCase("DESC")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Topic> topicPage;
        // Nếu có searchQuery thì tìm kiếm với phân trang, ngược lại thì tìm bình thường
        if (MyStringUtils.isNotNullAndNotEmpty(searchQuery)) {
            topicPage = wordCollectionRepository.findTopicsByIdAndNameContainingIgnoreCase(
                    collectionId, searchQuery, pageable);
        } else {
            topicPage = wordCollectionRepository.findTopicsById(collectionId, pageable);
        }

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
                .data(topicPage.map(topicMapper::toTopicResponse).toList())
                .build();
    }
}
