package com.server.wordwaves.service.implement;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.server.wordwaves.utils.PaginationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.server.wordwaves.config.CustomJwtDecoder;
import com.server.wordwaves.constant.PredefinedRole;
import com.server.wordwaves.dto.request.user.*;
import com.server.wordwaves.dto.response.auth.AuthenticationResponse;
import com.server.wordwaves.dto.response.common.EmailResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.user.UserResponse;
import com.server.wordwaves.entity.user.Role;
import com.server.wordwaves.entity.user.User;
import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.mapper.UserMapper;
import com.server.wordwaves.repository.RoleRepository;
import com.server.wordwaves.repository.UserRepository;
import com.server.wordwaves.service.EmailService;
import com.server.wordwaves.service.TokenService;
import com.server.wordwaves.service.UserService;
import com.server.wordwaves.utils.AuthUtils;
import com.server.wordwaves.utils.MyStringUtils;

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
    TokenService tokenService;
    AuthUtils authUtils;

    @Override
    public EmailResponse forgotPassword(ForgotPasswordRequest request) {
        User user = getUserByEmail(request.getEmail());
        return emailService.sendForgotPasswordEmail(user);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword()))
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        User user = validateTokenAndGetUser(request.getToken());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public EmailResponse register(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.EMAIL_EXISTED);
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return emailService.sendVerifyEmail(user);
    }

    @Override
    public ResponseEntity<AuthenticationResponse> verify(VerifyEmailRequest request) {
        String token = request.getToken();

        Jwt parsedToken = jwtDecoder.decode(token);
        String email = parsedToken.getSubject();
        String plainPassword = parsedToken.getClaim("ps");

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(plainPassword))
                .build();

        Set<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE.getName()).ifPresent(roles::add);

        user.setRoles(roles);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return authUtils.createAuthResponse(user);
    }

    private User validateTokenAndGetUser(String token) {
        tokenService.ensureTokenPresent(token);

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
    public PaginationInfo<List<UserResponse>> getUsers(int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery) {
        List<String> validSortByFields = Arrays.asList("fullName", "email", "createdAt");

        return PaginationUtils.getAllEntities(pageNumber, pageSize, sortBy, sortDirection, searchQuery, validSortByFields,
                pageable -> {
                    if (MyStringUtils.isNotNullAndNotEmpty(searchQuery)) return userRepository.findByFullNameContainingOrEmailContaining(searchQuery, searchQuery, pageable);
                    else return userRepository.findAll(pageable);
                },
                userMapper::toUserResponse
        );
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
    public UserResponse updateUserById(String userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String fullName = userUpdateRequest.getFullName();
        if (fullName != null && !fullName.isEmpty()) {
            user.setFullName(userUpdateRequest.getFullName());
        }

        String avatarName = userUpdateRequest.getAvatarName();
        if (avatarName != null && !avatarName.isEmpty()) {
            user.setAvatarName(avatarName);
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public void deleteUserById(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public void createOrUpdateUser(User user) {
        userRepository.save(user);
    }
}
