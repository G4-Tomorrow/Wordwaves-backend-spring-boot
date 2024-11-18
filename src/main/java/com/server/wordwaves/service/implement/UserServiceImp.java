package com.server.wordwaves.service.implement;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import com.server.wordwaves.dto.response.user.ChangeUserRoleResponse;
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
import com.server.wordwaves.utils.PaginationUtils;

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
        if (!request.getNewPassword().equals(request.getConfirmPassword())) throw new AppException(ErrorCode.PASSWORD_MISMATCH);
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

        return PaginationUtils.getAllEntities(
                pageNumber,
                pageSize,
                sortBy,
                sortDirection,
                searchQuery,
                validSortByFields,
                pageable -> {
                    if (MyStringUtils.isNotNullAndNotEmpty(searchQuery))
                        return userRepository.findByFullNameContainingOrEmailContaining(
                                searchQuery, searchQuery, pageable);
                    else return userRepository.findAll(pageable);
                },
                userMapper::toUserResponse);
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
        return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
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
        if (fullName != null && !fullName.isEmpty()) user.setFullName(userUpdateRequest.getFullName());

        String avatarName = userUpdateRequest.getAvatarName();
        if (avatarName != null && !avatarName.isEmpty()) user.setAvatarName(avatarName);

        User updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public void deleteUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        for (Role role : user.getRoles()) role.getUsers().remove(user);
        user.getRoles().clear();
        userRepository.delete(user);
    }

    @Override
    public ChangeUserRoleResponse changeRoleForUser(String userId, ChangeUserRoleRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED, "Người dùng không tồn tại với ID: " + userId));

        String type = request.getType().toUpperCase();
        List<String> requestedRoleNames = request.getRoleNames();

        List<Role> requestedRoles = roleRepository.findAllById(requestedRoleNames);
        Set<String> existingRoleNames = requestedRoles.stream().map(Role::getName).collect(Collectors.toSet());
        List<String> notFoundRoles = requestedRoleNames.stream().filter(roleName -> !existingRoleNames.contains(roleName)).collect(Collectors.toList());

        if (!notFoundRoles.isEmpty()) throw new AppException(ErrorCode.ROLE_NOT_EXISTED, "Các vai trò sau không tồn tại trên hệ thống: " + String.join(", ", notFoundRoles));

        Set<Role> currentRoles = user.getRoles();
        Set<String> currentRoleNames = currentRoles.stream().map(Role::getName).collect(Collectors.toSet());

        final Set<String> BASIC_ROLES = new HashSet<>(Arrays.asList("USER"));
        List<String> rolesAdded = new ArrayList<>(); List<String> rolesRemoved = new ArrayList<>();

        if ("ADD".equals(type)) {
            List<Role> rolesToAdd = requestedRoles.stream().filter(role -> !currentRoleNames.contains(role.getName())).collect(Collectors.toList());
            if (rolesToAdd.isEmpty()) throw new AppException(ErrorCode.NO_ROLES_TO_ADD);

            rolesToAdd.forEach(role -> {
                currentRoles.add(role);
                role.getUsers().add(user);
                rolesAdded.add(role.getName());
            });

        } else if ("REMOVE".equals(type)) {
            List<String> nonRemovableRoles = requestedRoleNames.stream().filter(BASIC_ROLES::contains).collect(Collectors.toList());

            if (!nonRemovableRoles.isEmpty()) throw new AppException(ErrorCode.CANNOT_REMOVE_BASIC_ROLE, "Không thể xóa vai trò cơ bản: " + String.join(", ", nonRemovableRoles));
            List<String> rolesUserDoesNotHave = requestedRoleNames.stream().filter(roleName -> !currentRoleNames.contains(roleName)).collect(Collectors.toList());

            if (!rolesUserDoesNotHave.isEmpty()) throw new AppException(ErrorCode.USER_DOES_NOT_HAVE_ROLE, "Người dùng không có các vai trò: " + String.join(", ", rolesUserDoesNotHave));
            List<Role> rolesToRemove = requestedRoles.stream().filter(role -> currentRoles.contains(role)).collect(Collectors.toList());

            if (rolesToRemove.isEmpty()) throw new AppException(ErrorCode.NO_ROLES_CAN_BE_REMOVED);
            rolesToRemove.forEach(role -> {
                currentRoles.remove(role);
                role.getUsers().remove(user);
                rolesRemoved.add(role.getName());
            });

            if (currentRoles.isEmpty()) {
                Role basicRole = roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
                currentRoles.add(basicRole);
                basicRole.getUsers().add(user);
                rolesAdded.add("USER");
            }
        } else throw new AppException(ErrorCode.INVALID_TYPE);

        userRepository.save(user);

        ChangeUserRoleResponse response = new ChangeUserRoleResponse();
        response.setRolesAdded(rolesAdded);
        response.setRolesRemoved(rolesRemoved);
        response.setCurrentRoles(currentRoles.stream().map(Role::getName).collect(Collectors.toList()));

        return response;
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
