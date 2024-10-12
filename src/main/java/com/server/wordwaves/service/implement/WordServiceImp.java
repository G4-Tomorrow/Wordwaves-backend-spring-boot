package com.server.wordwaves.service.implement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
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

        Word createdWord;
        try {
            createdWord = wordRepository.save(word);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.WORD_EXISTED);
        }

        if (topicOptional.isPresent()) {
            Topic topic = topicOptional.get();
            topic.getWords().add(createdWord);
            topicRepository.save(topic);
        }

        // Lấy từ điển
        List<WordResponse> wordResponses = null;
        try {
            wordResponses = dictionaryClient.retrieveEntries(word.getName());
        } catch (FeignException e) {

        }

        // Lấy từ phản hồi đầu tiên từ danh sách
        WordResponse wordResponse;
        if (!wordResponses.isEmpty()) {
            wordResponse = wordResponses.get(0);
            wordResponse.setId(word.getId());
            wordResponse.setName(word.getName());
            wordResponse.setThumbnailUrl(thumbnailUrl);
            wordResponse.setCreatedAt(word.getCreatedAt());
            wordResponse.setUpdatedAt(word.getUpdatedAt());
            wordResponse.setCreatedById(wordResponse.getCreatedById());
        } else {
            // Nếu từ vựng hợp lệ nhưng chưa có định nghĩa
            wordResponse = wordMapper.toWordResponse(word);
        }
        return wordResponse;
    }
}
