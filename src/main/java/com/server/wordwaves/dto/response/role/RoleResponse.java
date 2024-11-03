package com.server.wordwaves.dto.response.role;

import java.util.Set;

import com.server.wordwaves.dto.response.common.BaseResponse;
import com.server.wordwaves.dto.response.permission.PermissionResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse extends BaseResponse {
    String name;
    String description;
    Set<PermissionResponse> permissions;
}
