package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
import com.server.wordwaves.dto.request.vocabulary.WordUpdateRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import org.springframework.security.access.prepost.PreAuthorize;

public interface WordService {
    WordResponse create(WordCreationRequest request);

    PaginationInfo<List<WordResponse>> getWords(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection,
            String searchQuery,
            String userId,
            String isUnassigned);

    @PreAuthorize("@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).WORD).hasAccess(#wordId) || hasRole('ADMIN')")
    void deleteById(String wordId);

    @PreAuthorize("@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).WORD).hasAccess(#wordId) || hasRole('ADMIN')")
    WordResponse updateById(String wordId, WordUpdateRequest request);
}
