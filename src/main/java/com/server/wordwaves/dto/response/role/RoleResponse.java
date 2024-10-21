package com.server.wordwaves.dto.response.role;

import com.server.wordwaves.dto.response.common.BaseResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import com.server.wordwaves.entity.user.Permission;

import java.util.Set;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse extends BaseResponse {
    String name;
    String description;
    Set<Permission> permissions;
    String message;
}
