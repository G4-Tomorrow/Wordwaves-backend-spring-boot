package com.server.wordwaves.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseAuthor extends BaseEntity {

    String createdById;
    String updatedById;

    @PrePersist
    public void prePersist() {
        this.createdById = getCurrentUserId();
        this.updatedById = this.createdById;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedById = getCurrentUserId();
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return authentication.getName();
    }
}
