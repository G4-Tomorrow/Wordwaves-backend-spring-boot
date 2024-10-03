package com.server.wordwaves.dto.response;

import java.util.Set;

import com.server.wordwaves.entity.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String fullName;
    Set<Role> roles;
}
