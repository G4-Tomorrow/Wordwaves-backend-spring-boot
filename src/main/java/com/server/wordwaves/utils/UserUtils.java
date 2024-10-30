package com.server.wordwaves.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;

public class UserUtils {
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return authentication.getName();
    }
}
