package com.server.wordwaves.dto.response.user;

import java.time.Instant;
import java.util.Set;

import com.server.wordwaves.dto.response.common.BaseResponse;
import com.server.wordwaves.entity.user.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse extends BaseResponse {
    String id;
    String email;
    String fullName;
    String avatarName;
    Instant createdAt;
    Instant updatedAt;
    Set<Role> roles;
}
