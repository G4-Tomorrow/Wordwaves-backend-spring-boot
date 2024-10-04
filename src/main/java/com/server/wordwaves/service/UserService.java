package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.LogoutRequest;
import com.server.wordwaves.dto.request.UserCreationRequest;
import com.server.wordwaves.dto.request.UserUpdateRequest;
import com.server.wordwaves.dto.request.VerifyEmailRequest;
import com.server.wordwaves.dto.response.AuthenticationResponse;
import com.server.wordwaves.dto.response.EmailResponse;
import com.server.wordwaves.dto.response.UserResponse;
import com.server.wordwaves.entity.User;

public interface UserService {
    EmailResponse register(UserCreationRequest request);

    AuthenticationResponse verify(VerifyEmailRequest request);

    List<UserResponse> getUsers(int pageNumber, int pageSize, String sortBy, String sortDirection, String searchQuery);

    UserResponse getMyInfo();

    UserResponse getUserById(String userId);

    UserResponse updateUserById(String userId, UserUpdateRequest userUpdateRequest);

    User getUserByEmail(String email);

    void updateUserRefreshToken(String refreshToken, String email);

    void deleteUserById(String userId);

    void logout(LogoutRequest request);
}
