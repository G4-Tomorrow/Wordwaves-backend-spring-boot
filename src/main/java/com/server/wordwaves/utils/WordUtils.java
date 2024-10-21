package com.server.wordwaves.utils;

import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import com.server.wordwaves.entity.vocabulary.Word;
import com.server.wordwaves.mapper.WordMapper;
import com.server.wordwaves.repository.httpclient.DictionaryClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordUtils {
    DictionaryClient dictionaryClient;
    WordMapper wordMapper;

    public WordResponse getWordDetail(Word word) {
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
            wordResponse.setId(word.getId());
            wordResponse.setName(word.getName());
            wordResponse.setThumbnailUrl(word.getThumbnailUrl());
            wordResponse.setCreatedAt(word.getCreatedAt());
            wordResponse.setUpdatedAt(word.getUpdatedAt());
            wordResponse.setCreatedById(word.getCreatedById());
            wordResponse.setUpdatedById(word.getUpdatedById());
        } else {
            // Nếu từ vựng hợp lệ nhưng chưa có định nghĩa
            wordResponse = wordMapper.toWordResponse(word);
        }
        return wordResponse;
    }
}
