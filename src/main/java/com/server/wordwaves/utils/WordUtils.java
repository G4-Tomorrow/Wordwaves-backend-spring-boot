package com.server.wordwaves.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.server.wordwaves.dto.model.vocabulary.WordsTranslateModel;
import com.server.wordwaves.dto.response.vocabulary.WordsTranslateResponse;
import com.server.wordwaves.repository.httpclient.TranslateClient;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import com.server.wordwaves.entity.vocabulary.Word;
import com.server.wordwaves.mapper.WordMapper;
import com.server.wordwaves.repository.httpclient.DictionaryClient;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordUtils {
    DictionaryClient dictionaryClient;
    TranslateClient translateClient;
    WordMapper wordMapper;

    @Value("${app.translate-client.apikey}")
    @NonFinal
    String translateApiKey;

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

            if (wordResponse.getPhonetics() != null) {
                for (WordResponse.PhoneticModel phonetic : wordResponse.getPhonetics()) {
                    phonetic.setAccent(determineAccentByUrl(phonetic.getAudio()));
                }
            }
        } else {
            wordResponse = wordMapper.toWordResponse(word);
        }
        translateDefinitionsAndExamples(wordResponse);

        return wordResponse;
    }

    private void translateDefinitionsAndExamples(WordResponse wordResponse) {
        // Gom toàn bộ dữ liệu cần dịch
        List<String> textsToTranslate = new ArrayList<>();

        // Thêm tên từ vào danh sách để dịch
        textsToTranslate.add(wordResponse.getName());

        // Thêm các định nghĩa và ví dụ cần dịch
        for (WordResponse.MeaningModel meaning : wordResponse.getMeanings()) {
            for (WordResponse.DefinitionModel definition : meaning.getDefinitions()) {
                if (definition.getDefinition() != null) {
                    textsToTranslate.add(definition.getDefinition());
                }
                if (definition.getExample() != null) {
                    textsToTranslate.add(definition.getExample());
                }
            }
        }

        // Loại bỏ trùng lặp và kiểm tra dữ liệu rỗng
        textsToTranslate = textsToTranslate.stream()
                .filter(text -> text != null && !text.isEmpty())
                .distinct()
                .toList();

        if (!textsToTranslate.isEmpty()) {
            translateAndMapResults(textsToTranslate, wordResponse);
        }
    }

    private void translateAndMapResults(List<String> textsToTranslate, WordResponse wordResponse) {
        try {
            // Gửi toàn bộ danh sách cần dịch đến API
            WordsTranslateResponse response = translateClient.translate(
                    translateApiKey,
                    WordsTranslateModel.builder()
                            .platform("api")
                            .data(textsToTranslate)
                            .build()
            );

            // Xử lý kết quả trả về từ API
            List<String> translatedTexts = response.getResult();
            if (translatedTexts == null || translatedTexts.isEmpty()) {
                log.error("API returned no translations for texts: {}", textsToTranslate);
                return;
            }

            // Tạo ánh xạ từ bản gốc sang bản dịch
            Map<String, String> translationMap = new HashMap<>();
            for (int i = 0; i < textsToTranslate.size() && i < translatedTexts.size(); i++) {
                translationMap.put(textsToTranslate.get(i), translatedTexts.get(i));
            }

            // Gán bản dịch vào các trường tương ứng trong WordResponse
            wordResponse.setVietnamese(translationMap.getOrDefault(wordResponse.getName(), "Không có bản dịch."));

            wordResponse.getMeanings().forEach(meaning ->
                    meaning.getDefinitions().forEach(definition -> {
                        if (definition.getDefinition() != null) {
                            definition.setDefinitionMeaning(
                                    translationMap.getOrDefault(definition.getDefinition(), "Không có bản dịch.")
                            );
                        }
                        if (definition.getExample() != null) {
                            definition.setExampleMeaning(
                                    translationMap.getOrDefault(definition.getExample(), "Không có bản dịch.")
                            );
                        }
                    })
            );

        } catch (FeignException e) {
            log.error("!!!ERROR: Dịch thất bại: {}", e.getMessage());
            // Gán giá trị mặc định nếu API thất bại
            wordResponse.setVietnamese("Không có bản dịch.");
            wordResponse.getMeanings().forEach(meaning ->
                    meaning.getDefinitions().forEach(definition -> {
                        if (definition.getDefinition() != null) {
                            definition.setDefinitionMeaning("Không có bản dịch.");
                        }
                        if (definition.getExample() != null) {
                            definition.setExampleMeaning("Không có bản dịch.");
                        }
                    })
            );
        }
    }


    private String determineAccentByUrl(String audioUrl) {
        if (audioUrl != null) {
            if (audioUrl.contains("-us")) {
                return "US"; // Giọng đọc Mỹ
            } else if (audioUrl.contains("-uk")) {
                return "UK"; // Giọng đọc Anh
            }
        }
        return "Unknown";
    }

}
