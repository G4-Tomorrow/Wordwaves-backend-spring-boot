package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.role.RoleCreationRequest;
import com.server.wordwaves.dto.request.role.RoleUpdateRequest;
import com.server.wordwaves.dto.response.role.RoleResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;

public interface RoleService {
    RoleResponse createRole(RoleCreationRequest request);

    RoleResponse getRoleByName(String name);

    RoleResponse updateRole(String name, RoleUpdateRequest request);

    void deleteRole(String name);

    PaginationInfo<List<RoleResponse>> getRoles(int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery);

    void addPermissionsToRole(String roleName, List<String> permissionNames);
}
