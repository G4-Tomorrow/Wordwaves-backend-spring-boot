package com.server.wordwaves.service.implement;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.server.wordwaves.config.CustomJwtDecoder;
import com.server.wordwaves.config.JwtTokenProvider;
import com.server.wordwaves.dto.request.AuthenticationRequest;
import com.server.wordwaves.dto.request.IntrospectRequest;
import com.server.wordwaves.dto.response.AuthenticationResponse;
import com.server.wordwaves.dto.response.IntrospectResponse;
import com.server.wordwaves.dto.response.UserResponse;
import com.server.wordwaves.entity.User;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.UserMapper;
import com.server.wordwaves.service.AuthenticationService;
import com.server.wordwaves.service.BaseRedisService;
import com.server.wordwaves.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImp implements AuthenticationService {
    UserService userService;
    PasswordEncoder passwordEncoder;
    JwtTokenProvider jwtTokenProvider;
    CustomJwtDecoder jwtDecoder;
    UserMapper userMapper;
    BaseRedisService baseRedisService;

    @NonFinal
    @Value("${jwt.access-signer-key}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.access-token-duration-in-seconds}")
    protected long ACCESS_TOKEN_EXPIRATION;

    @NonFinal
    @Value("${jwt.refresh-token-duration-in-seconds}")
    protected long REFRESH_TOKEN_EXPIRATION;

    private ResponseEntity<AuthenticationResponse> createAuthResponse(User currentUser) {
        if (Objects.isNull(currentUser)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // Add information about current user login to response
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setUser(userMapper.toUserResponse(currentUser));

        // Create access token
        String accessToken = jwtTokenProvider.generateAccessToken(currentUser);
        authResponse.setAccessToken(accessToken);

        // Create refresh token and update refresh token to User entity
        String refreshToken = jwtTokenProvider.generateRefreshToken(currentUser);
        userService.updateUserRefreshToken(refreshToken, currentUser.getEmail());

        // Set cookies
        ResponseCookie resCookies = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(REFRESH_TOKEN_EXPIRATION)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(authResponse);
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        User currentUser = userService.getUserByEmail(request.getEmail());

        boolean authenticated = passwordEncoder.matches(request.getPassword(), currentUser.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        return createAuthResponse(currentUser);
    }

    @Override
    public ResponseEntity<AuthenticationResponse> getRefreshToken(String refreshToken)
            throws ParseException, JOSEException {
        // Check refresh token is valid
        SignedJWT decodedToken = jwtTokenProvider.verifyRefreshToken(refreshToken);
        String userId = decodedToken.getJWTClaimsSet().getSubject();

        // Check user by refresh token and userId
        User currentUser = this.userService.getUserByIdAndRefreshToken(userId, refreshToken);

        return createAuthResponse(currentUser);
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        String accessToken = request.getAccessToken();
        boolean isValid = true;
        try {
            jwtTokenProvider.verifyAccessToken(accessToken);
        } catch (AppException | JOSEException | ParseException e) {
            isValid = false;
            log.error("Error introspect: {}", e);
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public ResponseEntity<Void> logout(String token) {
        if (token == null || token.isEmpty()) throw new AppException(ErrorCode.EMPTY_TOKEN);
        Jwt jwt = null;
        try {
            jwt = jwtDecoder.decode(token);
        } catch (AppException e) {
            throw new AppException(e.getErrorCode());
        }
        Instant expiredDate = jwt.getExpiresAt();
        long timeRemaining = Duration.between(Instant.now(), expiredDate).toSeconds();

        if (timeRemaining <= 0) return ResponseEntity.ok().build();

        baseRedisService.set(token, "jwttoken");
        baseRedisService.setTimeToLive(token, timeRemaining);

        // Update refresh token is null in user entity
        UserResponse currentUser = this.userService.getMyInfo();
        this.userService.updateUserRefreshToken(null, currentUser.getEmail());

        // Remove refresh token in cookies
        ResponseCookie resDeleteCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resDeleteCookie.toString())
                .build();
    }
}
