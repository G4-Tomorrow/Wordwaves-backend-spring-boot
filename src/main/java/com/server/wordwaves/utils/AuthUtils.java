package com.server.wordwaves.utils;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.server.wordwaves.config.JwtTokenProvider;
import com.server.wordwaves.dto.response.auth.AuthenticationResponse;
import com.server.wordwaves.entity.user.User;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.UserMapper;
import com.server.wordwaves.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthUtils {
    UserMapper userMapper;
    JwtTokenProvider jwtTokenProvider;
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.refresh-token-duration-in-seconds}")
    protected long REFRESH_TOKEN_EXPIRATION;

    public ResponseEntity<AuthenticationResponse> createAuthResponse(User currentUser) {
        if (Objects.isNull(currentUser)) throw new AppException(ErrorCode.USER_NOT_EXISTED);

        // Add information about current user login to response
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setUser(userMapper.toUserResponse(currentUser));

        // Create access token
        String accessToken = jwtTokenProvider.generateAccessToken(currentUser);
        authResponse.setAccessToken(accessToken);

        // Create refresh token and update refresh token to User entity
        String refreshToken = jwtTokenProvider.generateRefreshToken(currentUser);
        this.updateUserRefreshToken(refreshToken, currentUser.getEmail());

        // Set cookies
        ResponseCookie resCookies = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false) // Chỉ dùng trên localhost
                .sameSite("None") // test
                .path("/")
                .maxAge(REFRESH_TOKEN_EXPIRATION)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(authResponse);
    }

    public void updateUserRefreshToken(String refreshToken, String email) {
        User currentUser =
                userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        currentUser.setRefreshToken(refreshToken);
        userRepository.save(currentUser);
    }
}
