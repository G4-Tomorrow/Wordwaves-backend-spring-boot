package com.server.wordwaves.service;

import java.util.List;
import com.server.wordwaves.dto.request.permission.PermissionCreationRequest;
import com.server.wordwaves.dto.request.permission.PermissionUpdateRequest;
import com.server.wordwaves.dto.response.permission.PermissionResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;

public interface PermissionService {
    PermissionResponse createPermission(PermissionCreationRequest request);

    PermissionResponse getPermissionByName(String name);

    PermissionResponse updatePermission(String name, PermissionUpdateRequest request);

    void deletePermission(String name);

    PaginationInfo<List<PermissionResponse>> getPermissions(int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery);
}
