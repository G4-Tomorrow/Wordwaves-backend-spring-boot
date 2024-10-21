package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.role.RoleCreationRequest;
import com.server.wordwaves.dto.request.role.RoleUpdateRequest;
import com.server.wordwaves.dto.response.role.RoleResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import org.springframework.security.access.prepost.PreAuthorize;

public interface RoleService {
    @PreAuthorize("hasRole('ADMIN')")
    RoleResponse createRole(RoleCreationRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    RoleResponse getRoleByName(String name);

    @PreAuthorize("hasRole('ADMIN')")
    RoleResponse updateRole(String name, RoleUpdateRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteRole(String name);

    @PreAuthorize("hasRole('ADMIN')")
    PaginationInfo<List<RoleResponse>> getRoles(int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery);

    @PreAuthorize("hasRole('ADMIN')")
    void addPermissionsToRole(String roleName, List<String> permissionNames);
}
