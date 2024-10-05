package com.server.wordwaves.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.server.wordwaves.dto.request.auth.LogoutRequest;
import com.server.wordwaves.dto.request.user.UserCreationRequest;
import com.server.wordwaves.dto.request.user.UserUpdateRequest;
import com.server.wordwaves.dto.request.user.VerifyEmailRequest;
import com.server.wordwaves.dto.response.auth.AuthenticationResponse;
import com.server.wordwaves.dto.response.common.EmailResponse;
import com.server.wordwaves.dto.response.common.PaginationInfo;
import com.server.wordwaves.dto.response.user.UserResponse;
import com.server.wordwaves.entity.User;

public interface UserService {
    EmailResponse register(UserCreationRequest request);

    AuthenticationResponse verify(VerifyEmailRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    PaginationInfo<List<UserResponse>> getUsers(
            int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery);

    UserResponse getMyInfo();

    UserResponse getUserById(String userId);

    UserResponse updateUserById(String userId, UserUpdateRequest userUpdateRequest);

    User getUserByEmail(String email);

    void updateUserRefreshToken(String refreshToken, String email);

    void deleteUserById(String userId);

    void logout(LogoutRequest request);
}
