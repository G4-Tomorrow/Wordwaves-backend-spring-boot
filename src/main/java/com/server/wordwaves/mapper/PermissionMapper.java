package com.server.wordwaves.mapper;

import com.server.wordwaves.dto.request.permission.PermissionCreationRequest;
import com.server.wordwaves.dto.response.permission.PermissionResponse;
import com.server.wordwaves.entity.user.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionCreationRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
