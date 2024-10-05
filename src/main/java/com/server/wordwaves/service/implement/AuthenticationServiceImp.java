package com.server.wordwaves.service.implement;

import java.text.ParseException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.server.wordwaves.config.JwtTokenProvider;
import com.server.wordwaves.dto.request.auth.AuthenticationRequest;
import com.server.wordwaves.dto.request.auth.IntrospectRequest;
import com.server.wordwaves.dto.response.auth.AuthenticationResponse;
import com.server.wordwaves.dto.response.auth.IntrospectResponse;
import com.server.wordwaves.entity.User;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.UserMapper;
import com.server.wordwaves.service.AuthenticationService;
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
    UserMapper userMapper;

    @NonFinal
    @Value("${jwt.access-signer-key}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.access-token-duration-in-seconds}")
    protected long ACCESS_TOKEN_EXPIRATION;



    @NonFinal
    @Value("${jwt.refresh-token-duration-in-seconds}")
    protected long REFRESH_TOKEN_EXPIRATION;



    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        User currentUser = userService.getUserByEmail(request.getEmail());

        boolean authenticated = passwordEncoder.matches(request.getPassword(), currentUser.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.WRONG_PASSWORD);

        // Add information about current user login to response
        AuthenticationResponse authResponse = new AuthenticationResponse();
        if (Objects.isNull(currentUser)) throw new AppException(ErrorCode.USER_NOT_EXISTED);
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
    public IntrospectResponse introspect(IntrospectRequest request) {
        String accessToken = request.getAccessToken();
        boolean isValid = true;
        try {
            jwtTokenProvider.verifyToken(accessToken);
        } catch (AppException | JOSEException | ParseException e) {
            isValid = false;
            log.error("Error introspect: {}", e);
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }
}
