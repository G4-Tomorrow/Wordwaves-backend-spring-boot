package com.server.wordwaves.strategy.checker.implement;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.server.wordwaves.repository.TopicRepository;
import com.server.wordwaves.strategy.checker.PermissionChecker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicPermissionChecker implements PermissionChecker {
    TopicRepository topicRepository;

    @Override
    public boolean hasAccess(String identifierId) {
        String createdById = topicRepository.findCreatedByIdById(identifierId);
        return Objects.equals(createdById, getCurrentUserId());
    }
}
