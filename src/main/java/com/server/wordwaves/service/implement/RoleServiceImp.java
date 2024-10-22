package com.server.wordwaves.service.implement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.server.wordwaves.utils.PaginationUtils;
import org.springframework.stereotype.Service;

import com.server.wordwaves.dto.request.role.RoleCreationRequest;
import com.server.wordwaves.dto.request.role.RoleUpdateRequest;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.role.RoleResponse;
import com.server.wordwaves.entity.user.Permission;
import com.server.wordwaves.entity.user.Role;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.RoleMapper;
import com.server.wordwaves.repository.PermissionRepository;
import com.server.wordwaves.repository.RoleRepository;
import com.server.wordwaves.service.RoleService;
import com.server.wordwaves.utils.MyStringUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImp implements RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleCreationRequest request) {
        if (roleRepository.existsByName(request.getName())) throw new AppException(ErrorCode.ROLE_EXISTED);
        Role role = roleMapper.toRole(request);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toRoleResponse(savedRole);
    }

    @Override
    public RoleResponse getRoleByName(String name) {
        Role role = roleRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public RoleResponse updateRole(String name, RoleUpdateRequest request) {
        Role role = roleRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        if (request.getDescription() != null && !request.getDescription().isEmpty()) role.setDescription(request.getDescription());
        List<String> permissionNames = request.getPermissionNames();

        if (permissionNames != null && !permissionNames.isEmpty()) {
            List<Permission> existingPermissions = permissionRepository.findAllById(permissionNames);
            Set<String> existingPermissionNames = existingPermissions.stream().map(Permission::getName).collect(Collectors.toSet());

            List<String> notExistingPermissions = permissionNames.stream().filter(permissionName -> !existingPermissionNames.contains(permissionName)).collect(Collectors.toList());

            if (!notExistingPermissions.isEmpty()) throw new AppException(ErrorCode.PERMISSION_NOT_EXISTED, "Các quyền hạn sau không tồn tại trên hệ thống: " + String.join(", ", notExistingPermissions));

            Set<String> existingPermissionsInRole = role.getPermissions().stream().map(Permission::getName).collect(Collectors.toSet());
            List<String> alreadyExistPermissions = permissionNames.stream().filter(existingPermissionsInRole::contains).collect(Collectors.toList());

            if (!alreadyExistPermissions.isEmpty()) throw new AppException(ErrorCode.PERMISSION_ALREADY_EXISTED, "Các quyền hạn sau đã tồn tại trong vai trò " + name + " : " + String.join(", ", alreadyExistPermissions));
            role.getPermissions().addAll(existingPermissions);
        }

        Role updatedRole = roleRepository.save(role);

        return roleMapper.toRoleResponse(updatedRole);
    }

    @Override
    public void deleteRole(String name) {
        if (!roleRepository.existsById(name)) throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        roleRepository.deleteById(name);
    }

    @Override
    public PaginationInfo<List<RoleResponse>> getRoles(int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery) {
        List<String> validSortByFields = Arrays.asList("name", "description", "createdAt");

        return PaginationUtils.getAllEntities(pageNumber, pageSize, sortBy, sortDirection, searchQuery, validSortByFields,
                pageable -> {
                    if (MyStringUtils.isNotNullAndNotEmpty(searchQuery)) return roleRepository.findByNameContainingOrDescriptionContaining(searchQuery, searchQuery, pageable);
                    else return roleRepository.findAll(pageable);
                },
                roleMapper::toRoleResponse
        );
    }
}
