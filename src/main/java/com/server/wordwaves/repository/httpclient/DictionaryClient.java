package com.server.wordwaves.repository.httpclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.server.wordwaves.dto.response.vocabulary.WordResponse;

@FeignClient(name = "dictionary-client", url = "${app.dictionary-client.url}")
public interface DictionaryClient {
    @GetMapping(value = "/{word}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<WordResponse> retrieveEntries(@PathVariable("word") String word);
}
