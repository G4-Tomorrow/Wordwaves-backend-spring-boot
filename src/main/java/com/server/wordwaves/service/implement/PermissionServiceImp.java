package com.server.wordwaves.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.server.wordwaves.dto.request.permission.PermissionCreationRequest;
import com.server.wordwaves.dto.request.permission.PermissionUpdateRequest;
import com.server.wordwaves.dto.response.common.Pagination;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.common.QueryOptions;
import com.server.wordwaves.dto.response.permission.PermissionResponse;
import com.server.wordwaves.entity.user.Permission;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.PermissionMapper;
import com.server.wordwaves.repository.PermissionRepository;
import com.server.wordwaves.service.PermissionService;
import com.server.wordwaves.utils.MyStringUtils;

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
        if (permissionRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        Permission permission = permissionMapper.toPermission(request);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(savedPermission);
    }

    @Override
    public PermissionResponse getPermissionByName(String name) {
        Permission permission = permissionRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public PermissionResponse updatePermission(String name, PermissionUpdateRequest request) {
        Permission permission = permissionRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        permission.setDescription(request.getDescription());
        Permission updatedPermission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(updatedPermission);
    }

    @Override
    public void deletePermission(String name) {
        if (!permissionRepository.existsById(name)) {
            throw new AppException(ErrorCode.PERMISSION_NOT_EXISTED);
        }
        permissionRepository.deleteById(name);
    }

    @Override
    public PaginationInfo<List<PermissionResponse>> getPermissions(int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery) {
        pageNumber--;
        Sort sort = MyStringUtils.isNullOrEmpty(sortBy)
                ? Sort.unsorted()
                : sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Permission> permissionsPage;

        if (MyStringUtils.isNotNullAndNotEmpty(searchQuery)) {
            permissionsPage = permissionRepository.findByNameContainingOrDescriptionContaining(searchQuery, searchQuery, pageable);
        } else {
            permissionsPage = permissionRepository.findAll(pageable);
        }

        List<PermissionResponse> permissions = permissionsPage.stream()
                .map(permissionMapper::toPermissionResponse)
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .pageNumber(++pageNumber)
                .pageSize(pageSize)
                .totalPages(permissionsPage.getTotalPages())
                .totalElements(permissionsPage.getTotalElements())
                .build();

        QueryOptions queryOptions = QueryOptions.builder()
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .searchQuery(searchQuery)
                .build();

        return PaginationInfo.<List<PermissionResponse>>builder()
                .pagination(pagination)
                .queryOptions(queryOptions)
                .data(permissions)
                .build();
    }
}
