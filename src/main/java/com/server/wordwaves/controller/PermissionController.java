package com.server.wordwaves.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.server.wordwaves.dto.request.permission.PermissionCreationRequest;
import com.server.wordwaves.dto.request.permission.PermissionUpdateRequest;
import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.permission.PermissionResponse;
import com.server.wordwaves.service.PermissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Permission Controller")
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    @Operation(summary = "TẠO QUYỀN HẠN")
    public ApiResponse<PermissionResponse> createPermission(@RequestBody @Valid PermissionCreationRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .message("Tạo quyền hạn thành công")
                .result(permissionService.createPermission(request))
                .build();
    }

    @GetMapping("/{name}")
    @Operation(summary = "LẤY QUYỀN HẠN THEO TÊN")
    public ApiResponse<PermissionResponse> getPermissionByName(@PathVariable String name) {
        return ApiResponse.<PermissionResponse>builder()
                .message("Lấy thông tin quyền hạn")
                .result(permissionService.getPermissionByName(name))
                .build();
    }

    @PutMapping("/{name}")
    @Operation(summary = "CẬP NHẬT QUYỀN HẠN")
    public ApiResponse<PermissionResponse> updatePermission(
            @PathVariable String name, @RequestBody @Valid PermissionUpdateRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .message("Cập nhật quyền hạn thành công")
                .result(permissionService.updatePermission(name, request))
                .build();
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "XÓA QUYỀN HẠN")
    public ApiResponse<Void> deletePermission(@PathVariable String name) {
        permissionService.deletePermission(name);
        return ApiResponse.<Void>builder().message("Xóa quyền hạn thành công").build();
    }

    @GetMapping
    @Operation(summary = "LẤY DANH SÁCH QUYỀN HẠN")
    public ApiResponse<PaginationInfo<List<PermissionResponse>>> getPermissions(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String searchQuery) {
        return ApiResponse.<PaginationInfo<List<PermissionResponse>>>builder()
                .message("Lấy danh sách quyền hạn")
                .result(permissionService.getPermissions(pageNumber, pageSize, sortBy, sortDirection, searchQuery))
                .build();
    }
}
