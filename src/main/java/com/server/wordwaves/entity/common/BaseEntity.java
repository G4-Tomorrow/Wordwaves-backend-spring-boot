package com.server.wordwaves.entity.common;

import java.time.Instant;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.*;
import lombok.experimental.FieldDefaults;

@MappedSuperclass
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;
}
