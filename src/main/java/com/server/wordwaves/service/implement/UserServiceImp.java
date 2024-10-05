package com.server.wordwaves.service.implement;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.server.wordwaves.config.CustomJwtDecoder;
import com.server.wordwaves.config.JwtTokenProvider;
import com.server.wordwaves.constant.PredefinedRole;
import com.server.wordwaves.dto.request.ResetPasswordRequest;
import com.server.wordwaves.dto.request.UserCreationRequest;
import com.server.wordwaves.dto.request.UserUpdateRequest;
import com.server.wordwaves.dto.response.AuthenticationResponse;
import com.server.wordwaves.dto.response.EmailResponse;
import com.server.wordwaves.dto.response.UserResponse;
import com.server.wordwaves.entity.Role;
import com.server.wordwaves.entity.User;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.UserMapper;
import com.server.wordwaves.repository.RoleRepository;
import com.server.wordwaves.repository.UserRepository;
import com.server.wordwaves.service.EmailService;
import com.server.wordwaves.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImp implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    CustomJwtDecoder jwtDecoder;
    PasswordEncoder passwordEncoder;
    EmailService emailService;
    UserMapper userMapper;
    JwtTokenProvider jwtTokenProvider;

    @Override
    public EmailResponse forgotPassword(String email) {
        User user = getUserByEmail(email);
        return emailService.sendForgotPasswordEmail(user);
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        User user = validateTokenAndGetUser(token);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public EmailResponse register(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.EMAIL_EXISTED);
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return emailService.sendVerifyEmail(user);
    }

    @Override
    public AuthenticationResponse verify(String token) {
        User user = validateTokenAndGetUser(token);

        Set<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE.getName()).ifPresent(roles::add);

        user.setRoles(roles);
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(user))
                .build();
    }

    private User validateTokenAndGetUser(String token) {
        if (token.isEmpty()) {
            throw new AppException(ErrorCode.EMPTY_TOKEN);
        }

        Jwt jwt;
        try {
            jwt = jwtDecoder.decode(token);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        Instant expiredDate = jwt.getExpiresAt();
        if (expiredDate.isBefore(Instant.now())) {
            throw new AppException(ErrorCode.EXPIRED_TOKEN);
        }

        String email = jwt.getSubject();
        return this.getUserByEmail(email);
    }

    @Override
    public List<UserResponse> getUsers() {
        return null;
    }

    @Override
    public UserResponse getMyInfo() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
                .findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    @PostAuthorize("returnObject.id == authentication.name || hasRole('ADMIN')")
    public UserResponse getUserById(String userId) {
        return userMapper.toUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public User getUserByIdAndRefreshToken(String userId, String refreshToken) {
        return userRepository
                .findByIdAndRefreshToken(userId, refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public void updateUserRefreshToken(String refreshToken, String email) {
        User currentUser =
                userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        currentUser.setRefreshToken(refreshToken);
        userRepository.save(currentUser);
    }

    @Override
    public UserResponse updateUserById(String userId, UserUpdateRequest userUpdateRequest) {
        return null;
    }

    @Override
    public void deleteUserById(String userId) {}
}
