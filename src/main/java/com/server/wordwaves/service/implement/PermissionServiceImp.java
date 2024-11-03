package com.server.wordwaves.service.implement;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.server.wordwaves.dto.request.permission.PermissionCreationRequest;
import com.server.wordwaves.dto.request.permission.PermissionUpdateRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.permission.PermissionResponse;
import com.server.wordwaves.entity.user.Permission;
import com.server.wordwaves.entity.user.Role;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.PermissionMapper;
import com.server.wordwaves.repository.PermissionRepository;
import com.server.wordwaves.service.PermissionService;
import com.server.wordwaves.utils.MyStringUtils;
import com.server.wordwaves.utils.PaginationUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImp implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionCreationRequest request) {
        if (permissionRepository.existsByName(request.getName())) throw new AppException(ErrorCode.PERMISSION_EXISTED);
        Permission permission = permissionMapper.toPermission(request);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(savedPermission);
    }

    @Override
    public PermissionResponse getPermissionByName(String name) {
        Permission permission = permissionRepository
                .findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public PermissionResponse updatePermission(String name, PermissionUpdateRequest request) {
        Permission permission = permissionRepository
                .findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        permission.setDescription(request.getDescription());
        Permission updatedPermission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(updatedPermission);
    }

    @Override
    public void deletePermission(String name) {
        Permission permission = permissionRepository
                .findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        for (Role role : permission.getRoles()) role.getPermissions().remove(permission);
        permission.getRoles().clear();
        permissionRepository.delete(permission);
    }

    @Override
    public PaginationInfo<List<PermissionResponse>> getPermissions(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery) {
        List<String> validSortByFields = Arrays.asList("name", "description", "createdAt");

        return PaginationUtils.getAllEntities(
                pageNumber,
                pageSize,
                sortBy,
                sortDirection,
                searchQuery,
                validSortByFields,
                pageable -> {
                    if (MyStringUtils.isNotNullAndNotEmpty(searchQuery))
                        return permissionRepository.findByNameContainingOrDescriptionContaining(
                                searchQuery, searchQuery, pageable);
                    else return permissionRepository.findAll(pageable);
                },
                permissionMapper::toPermissionResponse);
    }
}
