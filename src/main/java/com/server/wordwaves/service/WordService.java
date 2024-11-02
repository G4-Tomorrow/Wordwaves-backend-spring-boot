package com.server.wordwaves.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
import com.server.wordwaves.dto.request.vocabulary.WordUpdateRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;

public interface WordService {
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    WordResponse create(WordCreationRequest request);

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    PaginationInfo<List<WordResponse>> getWords(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection,
            String searchQuery,
            String userId,
            String isUnassigned);

    @PreAuthorize(
            "@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).WORD).hasAccess(#wordId) || hasRole('ADMIN')")
    void deleteById(String wordId);

    @PreAuthorize(
            "@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).WORD).hasAccess(#wordId) || hasRole('ADMIN')")
    WordResponse updateById(String wordId, WordUpdateRequest request);

    WordResponse detail(String name);
}
