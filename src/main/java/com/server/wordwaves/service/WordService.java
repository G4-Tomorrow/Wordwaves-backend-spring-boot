package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import org.springframework.security.access.prepost.PreAuthorize;

public interface WordService {
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    WordResponse create(WordCreationRequest request);

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    PaginationInfo<List<WordResponse>> getWords(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery);
}
