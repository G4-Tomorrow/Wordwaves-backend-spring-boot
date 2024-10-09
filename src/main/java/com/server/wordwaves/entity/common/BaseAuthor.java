package com.server.wordwaves.entity.common;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@MappedSuperclass
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseAuthor extends BaseEntity {

    String createdBy;

    @PrePersist
    public void prePersist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        this.createdBy = authentication.getName();
    }
}
