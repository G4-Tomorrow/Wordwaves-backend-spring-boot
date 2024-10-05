package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.user.*;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import com.server.wordwaves.dto.response.auth.AuthenticationResponse;
import com.server.wordwaves.dto.response.common.EmailResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.user.UserResponse;

import com.server.wordwaves.entity.User;

public interface UserService {
    EmailResponse forgotPassword(ForgotPasswordRequest request);

    void resetPassword(String token, ResetPasswordRequest request);

    EmailResponse register(UserCreationRequest request);

    AuthenticationResponse verify(VerifyEmailRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    PaginationInfo<List<UserResponse>> getUsers(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery);

    UserResponse getMyInfo();

    @PostAuthorize("returnObject.id == authentication.name || hasRole('ADMIN')")
    UserResponse getUserById(String userId);

    UserResponse updateUserById(String userId, UserUpdateRequest userUpdateRequest);

    User getUserByEmail(String email);

    void updateUserRefreshToken(String refreshToken, String email);

    void deleteUserById(String userId);

    User getUserByIdAndRefreshToken(String userId, String refreshToken);

}
