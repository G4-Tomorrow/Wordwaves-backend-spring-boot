package com.server.wordwaves.service;

import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import com.server.wordwaves.dto.request.vocabulary.WordCollectionAddTopicsRequest;
import com.server.wordwaves.dto.request.vocabulary.WordCollectionCreationRequest;
import com.server.wordwaves.dto.request.vocabulary.WordCollectionUpdateRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordCollectionResponse;
import org.springframework.security.access.prepost.PreAuthorize;

public interface WordCollectionService {
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    WordCollectionResponse create(WordCollectionCreationRequest request);

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostAuthorize("returnObject.data.first.createdById == authentication.name")
    PaginationInfo<List<WordCollectionResponse>> getCollections(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String userId);

    @PreAuthorize("@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).COLLECTION).hasAccess(#collectionId) || hasRole('ADMIN')")
    PaginationInfo<List<TopicResponse>> getTopics(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String collectionId);

    @PreAuthorize("@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).COLLECTION).hasAccess(#collectionId) || hasRole('ADMIN')")
    WordCollectionResponse updateById(String collectionId, WordCollectionUpdateRequest request);

    @PreAuthorize("@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).COLLECTION).hasAccess(#collectionId) || hasRole('ADMIN')")
    void deleteById(String collectionId);

    @PreAuthorize("@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).COLLECTION).hasAccess(#collectionId) || hasRole('ADMIN')")
    void addTopics(String collectionId, WordCollectionAddTopicsRequest request);
}
