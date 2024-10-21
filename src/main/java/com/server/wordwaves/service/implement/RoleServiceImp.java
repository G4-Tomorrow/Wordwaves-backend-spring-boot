package com.server.wordwaves.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.server.wordwaves.utils.ErrorMessageUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.server.wordwaves.dto.request.role.RoleCreationRequest;
import com.server.wordwaves.dto.request.role.RoleUpdateRequest;
import com.server.wordwaves.dto.response.common.Pagination;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.common.QueryOptions;
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
        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = roleMapper.toRole(request);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toRoleResponse(savedRole);
    }

    @Override
    public RoleResponse getRoleByName(String name) {
        Role role = roleRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public RoleResponse updateRole(String name, RoleUpdateRequest request) {
        Role role = roleRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            role.setDescription(request.getDescription());
        }

        List<String> permissionNames = request.getPermissionNames();

        Set<String> existingPermissionsInRole = role.getPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        List<String> notAddedPermissions = new ArrayList<>();
        List<String> alreadyExistPermissions = new ArrayList<>();
        List<String> addedPermissions = new ArrayList<>();

        if (permissionNames != null && !permissionNames.isEmpty()) {
            List<Permission> existingPermissions = permissionRepository.findAllById(permissionNames);

            Set<String> existingPermissionNames = existingPermissions.stream()
                    .map(Permission::getName)
                    .collect(Collectors.toSet());

            notAddedPermissions = permissionNames.stream()
                    .filter(permissionName -> !existingPermissionNames.contains(permissionName))
                    .collect(Collectors.toList());

            alreadyExistPermissions = permissionNames.stream()
                    .filter(existingPermissionsInRole::contains)
                    .collect(Collectors.toList());

            List<Permission> permissionsToAdd = existingPermissions.stream()
                    .filter(permission -> !existingPermissionsInRole.contains(permission.getName()))
                    .collect(Collectors.toList());

            role.getPermissions().addAll(permissionsToAdd);

            addedPermissions = permissionsToAdd.stream()
                    .map(Permission::getName)
                    .collect(Collectors.toList());
        }

        Role updatedRole = roleRepository.save(role);

        String message = ErrorMessageUtils.generateUpdateRoleMessage(name, addedPermissions, notAddedPermissions, alreadyExistPermissions);

        RoleResponse response = roleMapper.toRoleResponse(updatedRole);
        response.setMessage(message);

        return response;
    }

    @Override
    public void deleteRole(String name) {
        if (!roleRepository.existsById(name)) {
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        }
        roleRepository.deleteById(name);
    }

    @Override
    public PaginationInfo<List<RoleResponse>> getRoles(int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery) {
        pageNumber--;
        Sort sort = MyStringUtils.isNullOrEmpty(sortBy)
                ? Sort.unsorted()
                : sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Role> rolesPage;

        if (MyStringUtils.isNotNullAndNotEmpty(searchQuery)) {
            rolesPage = roleRepository.findByNameContainingOrDescriptionContaining(searchQuery, searchQuery, pageable);
        } else {
            rolesPage = roleRepository.findAll(pageable);
        }

        List<RoleResponse> roles = rolesPage.stream()
                .map(roleMapper::toRoleResponse)
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .pageNumber(++pageNumber)
                .pageSize(pageSize)
                .totalPages(rolesPage.getTotalPages())
                .totalElements(rolesPage.getTotalElements())
                .build();

        QueryOptions queryOptions = QueryOptions.builder()
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .searchQuery(searchQuery)
                .build();

        return PaginationInfo.<List<RoleResponse>>builder()
                .pagination(pagination)
                .queryOptions(queryOptions)
                .data(roles)
                .build();
    }
}
