package com.server.wordwaves.mapper;

import com.server.wordwaves.dto.request.permission.PermissionRequest;
import com.server.wordwaves.dto.request.role.RoleRequest;
import com.server.wordwaves.dto.response.permission.PermissionResponse;
import com.server.wordwaves.dto.response.role.RoleResponse;
import com.server.wordwaves.entity.user.Permission;
import com.server.wordwaves.entity.user.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
