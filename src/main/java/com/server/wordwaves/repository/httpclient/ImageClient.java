package com.server.wordwaves.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.server.wordwaves.dto.response.vocabulary.WordThumbnailResponse;

@FeignClient(name = "image-client", url = "${app.pexels-client.url}")
public interface ImageClient {
    @GetMapping
    WordThumbnailResponse retrieveWordThumbnailUrl(
            @RequestHeader("Authorization") String apiKey,
            @RequestParam("query") String query,
            @RequestParam(value = "per_page", defaultValue = "1") int pageSize);
}
