package com.server.wordwaves.mapper;

import org.mapstruct.Mapper;
import com.server.wordwaves.dto.request.role.RoleCreationRequest;
import com.server.wordwaves.dto.response.role.RoleResponse;
import com.server.wordwaves.entity.user.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toRole(RoleCreationRequest request);

    RoleResponse toRoleResponse(Role role);
}
