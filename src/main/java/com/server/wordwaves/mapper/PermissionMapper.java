package com.server.wordwaves.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.server.wordwaves.dto.request.permission.PermissionCreationRequest;
import com.server.wordwaves.dto.response.permission.PermissionResponse;
import com.server.wordwaves.entity.user.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreationRequest request);

    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    PermissionResponse toPermissionResponse(Permission permission);
}
