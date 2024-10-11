package com.server.wordwaves.service;

import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;

import java.io.IOException;

public interface WordService {
    WordResponse create(WordCreationRequest request);
}
