package com.server.wordwaves.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.server.wordwaves.dto.model.vocabulary.WordsTranslateModel;
import com.server.wordwaves.dto.response.vocabulary.WordsTranslateResponse;

@FeignClient(name = "translate-client", url = "${app.translate-client.url}")
public interface TranslateClient {
    @PostMapping
    WordsTranslateResponse translate(
            @RequestHeader("Authorization") String apiKey, @RequestBody WordsTranslateModel model);
}
