package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
import com.server.wordwaves.dto.request.vocabulary.WordUpdateRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;

public interface WordService {
    WordResponse create(WordCreationRequest request);

    PaginationInfo<List<WordResponse>> getWords(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String isUnassigned);

    void deleteById(String wordId);

    WordResponse updateById(String wordId, WordUpdateRequest request);
}
