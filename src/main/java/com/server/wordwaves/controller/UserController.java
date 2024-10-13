package com.server.wordwaves.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.server.wordwaves.dto.request.user.ForgotPasswordRequest;
import com.server.wordwaves.dto.request.user.ResetPasswordRequest;
import com.server.wordwaves.dto.request.user.UserCreationRequest;
import com.server.wordwaves.dto.request.user.UserUpdateRequest;
import com.server.wordwaves.dto.request.user.VerifyEmailRequest;
import com.server.wordwaves.dto.response.auth.AuthenticationResponse;
import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.dto.response.common.EmailResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.user.UserResponse;
import com.server.wordwaves.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "User Controller")
public class UserController {
    UserService userService;

    @PostMapping
    @Operation(summary = "REGISTER", description = "Api Create New User")
    ApiResponse<EmailResponse> register(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<EmailResponse>builder()
                .message("Email xác thực đã được gửi thành công")
                .result(userService.register(request))
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<EmailResponse> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        return ApiResponse.<EmailResponse>builder()
                .result(userService.forgotPassword(request))
                .message("Yêu cầu đặt lại mật khẩu thành công.")
                .build();
    }

    @PutMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ApiResponse.<String>builder()
                .message("Mật khẩu đã được cập nhật thành công.")
                .build();
    }

    @PostMapping("/verify")
    ApiResponse<AuthenticationResponse> verify(@RequestBody VerifyEmailRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Xác thực email thành công")
                .result(userService.verify(request))
                .build();
    }

    @GetMapping
    ApiResponse<PaginationInfo<List<UserResponse>>> getUsers(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String searchQuery) {
        return ApiResponse.<PaginationInfo<List<UserResponse>>>builder()
                .message("Lấy nhiều người dùng")
                .result(userService.getUsers(pageNumber, pageSize, sortBy, sortDirection, searchQuery))
                .build();
    }

    @GetMapping("/myinfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .message("Lấy thông tin người dùng")
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .message("Lấy thông tin người dùng qua id")
                .result(userService.getUserById(userId))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUserById(
            @PathVariable String userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        return ApiResponse.<UserResponse>builder()
                .message("Cập nhập thông tin thành công")
                .result(userService.updateUserById(userId, userUpdateRequest))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<Void> deleteUserById(@PathVariable String userId) {
        userService.deleteUserById(userId);
        return ApiResponse.<Void>builder().message("Xóa người dùng thành công").build();
    }
}
