package com.server.wordwaves.security.checker.implement;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.server.wordwaves.repository.WordCollectionRepository;
import com.server.wordwaves.security.checker.PermissionChecker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordCollectionPermissionChecker implements PermissionChecker {
    WordCollectionRepository wordCollectionRepository;

    @Override
    public boolean hasAccess(String identifierId) {
        String createdById = wordCollectionRepository.findCreatedByIdById(identifierId);
        log.info("identifierId: {}", identifierId);
        log.info("createdById: {}", createdById);
        return Objects.equals(createdById, getCurrentUserId());
    }
}
