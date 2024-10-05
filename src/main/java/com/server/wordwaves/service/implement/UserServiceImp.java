package com.server.wordwaves.service.implement;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.server.wordwaves.dto.request.LogoutRequest;
import com.server.wordwaves.dto.request.VerifyEmailRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.server.wordwaves.config.CustomJwtDecoder;
import com.server.wordwaves.config.JwtTokenProvider;
import com.server.wordwaves.constant.PredefinedRole;
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
import com.server.wordwaves.service.BaseRedisService;
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
    BaseRedisService baseRedisService;

    @Override
    public EmailResponse register(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.EMAIL_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return emailService.sendVerifyEmail(user);
    }

    @Override
    public AuthenticationResponse verify(VerifyEmailRequest request) {
        String token = request.getToken();
        if (token.isEmpty()) throw new AppException(ErrorCode.EMPTY_TOKEN);

        Jwt jwt = jwtDecoder.decode(token);
        Instant expiredDate = jwt.getExpiresAt();
        String email = jwt.getSubject();
        String password = jwt.getClaim("ps");

        if (expiredDate.isBefore(new Date().toInstant())) throw new AppException(ErrorCode.EXPIRED_TOKEN);

        // tạo user
        Set<Role> roles = new HashSet<>();
        User user = User.builder().email(email).password(password).build();
        roleRepository.findById(PredefinedRole.USER_ROLE.getName()).ifPresent(roles::add);

        user.setRoles(roles);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return AuthenticationResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(user))
                .user(userMapper.toUserResponse(user))
                .build();
    }

    @Override
    public List<UserResponse> getUsers(int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery) {
        pageNumber--;
        Sort sort = sortDirection.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> usersPage;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            usersPage = userRepository.findByFullNameContainingOrEmailContaining(searchQuery, searchQuery, pageable);
        } else {
            usersPage = userRepository.findAll(pageable);
        }

        return usersPage.stream()
                .map(userMapper::toUserResponse).toList();
    }


    @Override
    public UserResponse getMyInfo() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
                .findById(name)
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
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
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
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (userUpdateRequest.getFullName() != null && !userUpdateRequest.getFullName().isEmpty()) {
            user.setFullName(userUpdateRequest.getFullName());
        }

        if (userUpdateRequest.getRole() != null && !userUpdateRequest.getRole().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findById(userUpdateRequest.getRole()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST)));
            user.setRoles(roles);
        }

        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .roles(user.getRoles())
                .build();
    }

    @Override
    public void deleteUserById(String userId) {}

    @Override
    public void logout(LogoutRequest request) {
        String token = request.getToken();
        if (token == null || token.isEmpty()) throw new AppException(ErrorCode.EMPTY_TOKEN);
        Jwt jwt = null;
        token = token.substring(7);
        try {
            jwt = jwtDecoder.decode(token);
        } catch (AppException e) {
            throw new AppException(e.getErrorCode());
        }
        Instant expiredDate = jwt.getExpiresAt();
        long timeRemaining = Duration.between(Instant.now(), expiredDate).toSeconds();

        if (timeRemaining <= 0) return;

        baseRedisService.set(token, "jwttoken");
        baseRedisService.setTimeToLive(token, timeRemaining);
    }
}
