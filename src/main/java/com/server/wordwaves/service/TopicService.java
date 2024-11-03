package com.server.wordwaves.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.server.wordwaves.dto.request.vocabulary.TopicAddWordsRequest;
import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.request.vocabulary.TopicUpdateRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;

public interface TopicService {
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    TopicResponse create(TopicCreationRequest request);

    @PreAuthorize(
            "@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).TOPIC).hasAccess(#topicId) || hasRole('ADMIN')")
    PaginationInfo<List<WordResponse>> getWords(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String topicId);

    @PreAuthorize(
            "@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).TOPIC).hasAccess(#topicId) || hasRole('ADMIN')")
    void addWords(String topicId, TopicAddWordsRequest request);

    @PreAuthorize(
            "@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).TOPIC).hasAccess(#topicId) || hasRole('ADMIN')")
    TopicResponse updateById(String topicId, TopicUpdateRequest request);

    @PreAuthorize(
            "@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).TOPIC).hasAccess(#topicId) || hasRole('ADMIN')")
    void deleteById(String topicId);

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    PaginationInfo<List<TopicResponse>> getTopics(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String userId);
}
