package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.vocabulary.TopicAddWordsRequest;
import com.server.wordwaves.dto.request.vocabulary.TopicCreationRequest;
import com.server.wordwaves.dto.request.vocabulary.TopicUpdateRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.vocabulary.TopicResponse;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

public interface TopicService {
    TopicResponse create(TopicCreationRequest request);

    @PreAuthorize("@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).TOPIC).hasAccess(#topicId) || hasRole('ADMIN')")
    PaginationInfo<List<WordResponse>> getWords(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String topicId);

    @PreAuthorize("@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).TOPIC).hasAccess(#topicId) || hasRole('ADMIN')")
    void addWords(String topicId, TopicAddWordsRequest request);

    @PreAuthorize("@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).TOPIC).hasAccess(#topicId) || hasRole('ADMIN')")
    TopicResponse updateById(String topicId, TopicUpdateRequest request);

    @PreAuthorize("@permissionCheckerFactory.getPermissionChecker(T(com.server.wordwaves.constant.PermissionType).TOPIC).hasAccess(#topicId) || hasRole('ADMIN')")
    void deleteById(String topicId);

    PaginationInfo<List<TopicResponse>> getTopics(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery, String userId);
}
