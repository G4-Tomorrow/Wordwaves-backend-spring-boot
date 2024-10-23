package com.server.wordwaves.security.checker.implement;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.server.wordwaves.repository.WordRepository;
import com.server.wordwaves.security.checker.PermissionChecker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WordPermissionChecker implements PermissionChecker {
    WordRepository wordRepository;

    @Override
    public boolean hasAccess(String identifierId) {
        String createdById = wordRepository.findCreatedByIdById(identifierId);
        return Objects.equals(createdById, getCurrentUserId());
    }
}
