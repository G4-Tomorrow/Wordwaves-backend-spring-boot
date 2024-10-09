package com.server.wordwaves.service.implement;

import com.server.wordwaves.dto.request.vocabulary.WordCollectionCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.WordCollectionCreationResponse;
import com.server.wordwaves.entity.vocabulary.WordCollection;
import com.server.wordwaves.entity.vocabulary.WordCollectionCategory;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.WordCollectionMapper;
import com.server.wordwaves.repository.WordCollectionCategoryRepository;
import com.server.wordwaves.repository.WordCollectionRepository;
import com.server.wordwaves.service.WordCollectionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordCollectionServiceImp implements WordCollectionService {
    WordCollectionRepository wordCollectionRepository;
    WordCollectionMapper wordCollectionMapper;
    WordCollectionCategoryRepository wordCollectionCategoryRepository;

    @Override
    public WordCollectionCreationResponse create(WordCollectionCreationRequest request) {
        String category = request.getCategory();
        WordCollection wordCollection = wordCollectionMapper.toWordCollection(request);

        // check xem category có null hay ko
        if (category != null && !category.isEmpty()) {
            Optional<WordCollectionCategory> wordCollectionCategoryOptional = wordCollectionCategoryRepository.findByName(category);

            if (wordCollectionCategoryOptional.isPresent()) {
                // xử lí nếu category đã tồn tại
                wordCollection.setWordCollectionCategory(wordCollectionCategoryOptional.get());
            } else {
                // xử lí nếu category chưa tồn tại
                WordCollectionCategory wordCollectionCategory = wordCollectionCategoryRepository.save(WordCollectionCategory.builder()
                        .name(category)
                        .build());
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
}
