package com.server.wordwaves.service;

import java.util.List;

import com.server.wordwaves.dto.request.ResetPasswordRequest;
import com.server.wordwaves.dto.request.UserCreationRequest;
import com.server.wordwaves.dto.request.UserUpdateRequest;
import com.server.wordwaves.dto.response.AuthenticationResponse;
import com.server.wordwaves.dto.response.EmailResponse;
import com.server.wordwaves.dto.response.UserResponse;
import com.server.wordwaves.entity.User;

public interface UserService {
    EmailResponse forgotPassword(String email);

    void resetPassword(String token, ResetPasswordRequest request);

    EmailResponse register(UserCreationRequest request);

    AuthenticationResponse verify(String token);

    List<UserResponse> getUsers();

    UserResponse getMyInfo();

    UserResponse getUserById(String userId);

    UserResponse updateUserById(String userId, UserUpdateRequest userUpdateRequest);

    User getUserByEmail(String email);

    void updateUserRefreshToken(String refreshToken, String email);

    void deleteUserById(String userId);

    User getUserByIdAndRefreshToken(String userId, String refreshToken);
}
