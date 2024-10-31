package com.server.wordwaves.controller;

import java.text.ParseException;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.nimbusds.jose.JOSEException;
import com.server.wordwaves.dto.request.auth.AuthenticationRequest;
import com.server.wordwaves.dto.request.auth.IntrospectRequest;
import com.server.wordwaves.dto.request.auth.LogoutRequest;
import com.server.wordwaves.dto.request.auth.RefreshTokenRequest;
import com.server.wordwaves.dto.response.auth.AuthenticationResponse;
import com.server.wordwaves.dto.response.auth.IntrospectResponse;
import com.server.wordwaves.dto.response.common.ApiResponse;
import com.server.wordwaves.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @GetMapping("/oauth2/login-success")
    @Operation(summary = "ĐĂNG NHẬP OAUTH2")
    ResponseEntity<ApiResponse<AuthenticationResponse>> oauth2Login(
            OAuth2AuthenticationToken oauth2AuthenticationToken) {
        ResponseEntity<AuthenticationResponse> responseEntity =
                authenticationService.oauth2Authenticate(oauth2AuthenticationToken);

        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .message("Đăng nhập qua OAuth2")
                .result(responseEntity.getBody())
                .build();

        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(apiResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "ĐĂNG NHẬP QUA EMAIL")
    ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody @Valid AuthenticationRequest request) {
        ResponseEntity<AuthenticationResponse> responseEntity = authenticationService.authenticate(request);
        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .message("Đăng nhập")
                .result(responseEntity.getBody())
                .build();

        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(apiResponse);
    }

    @PostMapping("/introspect")
    @Operation(summary = "KIỂM TRA TÍNH KHẢ THI CỦA TOKEN")
    ApiResponse<IntrospectResponse> introspect(@RequestBody @Valid IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .message("Kiểm tra token đã hết hạn hoặc bị logout chưa")
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/logout")
    @Operation(summary = "ĐĂNG XUẤT")
    ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") LogoutRequest request) {
        ResponseEntity<Void> responseEntity = authenticationService.logout(request);
        ApiResponse<Void> apiResponse =
                ApiResponse.<Void>builder().message("Đăng xuất thành công").build();

        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(apiResponse);
    }

    @GetMapping("/refresh")
    @Operation(summary = "LÀM MỚI TOKEN")
    ResponseEntity<ApiResponse<AuthenticationResponse>> refresh(
            @CookieValue(name = "refresh_token") RefreshTokenRequest request) throws ParseException, JOSEException {
        ResponseEntity<AuthenticationResponse> responseEntity = authenticationService.getRefreshToken(request);
        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .message("Refresh token")
                .result(responseEntity.getBody())
                .build();

        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(apiResponse);
    }
}
