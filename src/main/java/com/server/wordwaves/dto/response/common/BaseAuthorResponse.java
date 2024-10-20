package com.server.wordwaves.dto.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseAuthorResponse extends BaseResponse {
    String createdById;
    String updatedById;
}
