package com.server.wordwaves.strategy.checker;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface PermissionChecker {
    boolean hasAccess(String identifierId);

    default String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
