package com.server.wordwaves.dto.response.common;

import java.time.Instant;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseResponse {
    Instant createdAt;
    Instant updatedAt;
}
