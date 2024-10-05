package com.server.wordwaves.controller;

import java.util.List;

import com.server.wordwaves.dto.request.ForgotPasswordRequest;
import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.server.wordwaves.dto.ApiResponse;
import com.server.wordwaves.dto.request.ResetPasswordRequest;
import com.server.wordwaves.dto.request.UserCreationRequest;
import com.server.wordwaves.dto.request.UserUpdateRequest;
import com.server.wordwaves.dto.response.AuthenticationResponse;
import com.server.wordwaves.dto.response.EmailResponse;
import com.server.wordwaves.dto.response.UserResponse;
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
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<EmailResponse> register(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<EmailResponse>builder()
                .result(userService.register(request))
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<EmailResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return ApiResponse.<EmailResponse>builder()
                .result(userService.forgotPassword(request.getEmail()))
                .message("Yêu cầu đặt lại mật khẩu thành công.")
                .build();
    }

    @PutMapping("/reset-password")
    public ApiResponse<String> resetPassword(
            @RequestParam String token, @RequestBody @Valid ResetPasswordRequest request) {
        userService.resetPassword(token, request);
        return ApiResponse.<String>builder()
                .message("Mật khẩu đã được cập nhật thành công.")
                .build();
    }

    @GetMapping("/verify")
    ApiResponse<AuthenticationResponse> verify(@RequestParam String token) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(userService.verify(token))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/myinfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable String userId) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUserById(userId));
        return apiResponse;
    }

    @PutMapping("/{userId}")
    UserResponse updateUserById(@PathVariable String userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.updateUserById(userId, userUpdateRequest);
    }

    @DeleteMapping("/{userId}")
    String deleteUserById(@PathVariable String userId) {
        userService.deleteUserById(userId);
        return "User has been deleted";
    }
}
