package com.server.wordwaves.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.server.wordwaves.dto.request.role.RoleCreationRequest;
import com.server.wordwaves.dto.request.role.RoleUpdateRequest;
import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.role.RoleResponse;
import com.server.wordwaves.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Role Controller")
public class RoleController {

    RoleService roleService;

    @PostMapping
    @Operation(summary = "TẠO VAI TRÒ")
    public ApiResponse<RoleResponse> createRole(@RequestBody @Valid RoleCreationRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .message("Tạo vai trò thành công")
                .result(roleService.createRole(request))
                .build();
    }

    @GetMapping("/{name}")
    @Operation(summary = "LẤY VAI TRÒ THEO TÊN")
    public ApiResponse<RoleResponse> getRoleByName(@PathVariable String name) {
        return ApiResponse.<RoleResponse>builder()
                .message("Lấy thông tin vai trò")
                .result(roleService.getRoleByName(name))
                .build();
    }

    @PutMapping("/{name}")
    @Operation(summary = "CẬP NHẬT VAI TRÒ")
    public ApiResponse<RoleResponse> updateRole(@PathVariable String name, @RequestBody @Valid RoleUpdateRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .message("Cập nhật vai trò thành công")
                .result(roleService.updateRole(name, request))
                .build();
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "XÓA VAI TRÒ")
    public ApiResponse<Void> deleteRole(@PathVariable String name) {
        roleService.deleteRole(name);
        return ApiResponse.<Void>builder()
                .message("Xóa vai trò thành công")
                .build();
    }

    @GetMapping
    @Operation(summary = "LẤY DANH SÁCH VAI TRÒ")
    public ApiResponse<PaginationInfo<List<RoleResponse>>> getRoles(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String searchQuery) {
        return ApiResponse.<PaginationInfo<List<RoleResponse>>>builder()
                .message("Lấy danh sách vai trò")
                .result(roleService.getRoles(pageNumber, pageSize, sortBy, sortDirection, searchQuery))
                .build();
    }

    @PostMapping("/{name}/permissions")
    @Operation(summary = "THÊM QUYỀN HẠN VÀO VAI TRÒ")
    public ApiResponse<Void> addPermissionsToRole(@PathVariable String name, @RequestBody List<String> permissionNames) {
        roleService.addPermissionsToRole(name, permissionNames);
        return ApiResponse.<Void>builder()
                .message("Thêm quyền hạn vào vai trò thành công")
                .build();
    }
}
