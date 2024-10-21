package com.server.wordwaves.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import com.server.wordwaves.dto.request.user.*;
import com.server.wordwaves.dto.response.auth.AuthenticationResponse;
import com.server.wordwaves.dto.response.common.EmailResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.user.UserResponse;
import com.server.wordwaves.entity.user.User;

public interface UserService {
    EmailResponse forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    EmailResponse register(UserCreationRequest request);

    ResponseEntity<AuthenticationResponse> verify(VerifyEmailRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    PaginationInfo<List<UserResponse>> getUsers(int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery);

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    UserResponse getMyInfo();

    @PostAuthorize("returnObject.id == authentication.name || hasRole('ADMIN')")
    UserResponse getUserById(String userId);

    @PostAuthorize("returnObject.id == authentication.name || hasRole('ADMIN')")
    UserResponse updateUserById(String userId, UserUpdateRequest userUpdateRequest);

    User getUserByEmail(String email);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteUserById(String userId);

    User getUserByIdAndRefreshToken(String userId, String refreshToken);

    boolean existsUserByEmail(String email);

    void createOrUpdateUser(User user);
}
