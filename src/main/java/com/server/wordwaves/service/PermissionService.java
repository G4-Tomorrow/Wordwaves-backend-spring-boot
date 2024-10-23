package com.server.wordwaves.service;

import java.util.List;
import com.server.wordwaves.dto.request.permission.PermissionCreationRequest;
import com.server.wordwaves.dto.request.permission.PermissionUpdateRequest;
import com.server.wordwaves.dto.response.permission.PermissionResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import org.springframework.security.access.prepost.PreAuthorize;

public interface PermissionService {
    @PreAuthorize("hasRole('ADMIN')")
    PermissionResponse createPermission(PermissionCreationRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    PermissionResponse getPermissionByName(String name);

    @PreAuthorize("hasRole('ADMIN')")
    PermissionResponse updatePermission(String name, PermissionUpdateRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    void deletePermission(String name);

    @PreAuthorize("hasRole('ADMIN')")
    PaginationInfo<List<PermissionResponse>> getPermissions(int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery);
}
