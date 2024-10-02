package com.server.wordwaves.service.implement;

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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public AuthenticationResponse verify(String token) {
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
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtTokenProvider.generateToken(user))
                .build();
    }

    @Override
    public List<UserResponse> getUsers() {
        return null;
    }

    @Override
    public UserResponse getMyInfo() {
        return null;
    }

    @Override
    public UserResponse getUserById(String userId) {
        return null;
    }

    @Override
    public UserResponse updateUserById(String userId, UserUpdateRequest userUpdateRequest) {
        return null;
    }

    @Override
    public void deleteUserById(String userId) {

    }

    @Override
    public void logout(String token) {
        if (token == null || token.isEmpty())
            throw new AppException(ErrorCode.EMPTY_TOKEN);
        Jwt jwt = null;
        try {
            jwt = jwtDecoder.decode(token);
        } catch (AppException e) {
            throw new AppException(e.getErrorCode());
        }
        Instant expiredDate = jwt.getExpiresAt();
        long timeRemaining = Duration.between(Instant.now(), expiredDate).toSeconds();

        if(timeRemaining <= 0) return;

        baseRedisService.set(token, "jwttoken");
        baseRedisService.setTimeToLive(token, timeRemaining);
    }
}
