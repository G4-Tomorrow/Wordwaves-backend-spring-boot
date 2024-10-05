package com.server.wordwaves.controller;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nimbusds.jose.JOSEException;
import com.server.wordwaves.dto.ApiResponse;
import com.server.wordwaves.dto.request.AuthenticationRequest;
import com.server.wordwaves.dto.request.IntrospectRequest;
import com.server.wordwaves.dto.response.AuthenticationResponse;
import com.server.wordwaves.dto.response.IntrospectResponse;
import com.server.wordwaves.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest request) {
        ResponseEntity<AuthenticationResponse> responseEntity = authenticationService.authenticate(request);
        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .result(responseEntity.getBody())
                .build();

        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(apiResponse);
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/logout")
    ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        ResponseEntity<Void> responseEntity = authenticationService.logout(token.substring(7));
        ApiResponse<Void> apiResponse =
                ApiResponse.<Void>builder().message("Đăng xuất thành công").build();

        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(apiResponse);
    }

    @GetMapping("/refresh")
    ResponseEntity<ApiResponse<AuthenticationResponse>> refresh(
            @CookieValue(name = "refresh_token") String refreshToken) throws ParseException, JOSEException {
        ResponseEntity<AuthenticationResponse> responseEntity = authenticationService.getRefreshToken(refreshToken);
        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .result(responseEntity.getBody())
                .build();

        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(apiResponse);
    }
}
