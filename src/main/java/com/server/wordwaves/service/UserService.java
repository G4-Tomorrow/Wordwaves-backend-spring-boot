package com.server.wordwaves.service;

import com.server.wordwaves.dto.request.UserCreationRequest;
import com.server.wordwaves.dto.request.UserUpdateRequest;
import com.server.wordwaves.dto.response.AuthenticationResponse;
import com.server.wordwaves.dto.response.EmailResponse;
import com.server.wordwaves.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    EmailResponse register(UserCreationRequest request);

    AuthenticationResponse verify(String token);

    List<UserResponse> getUsers();

    UserResponse getMyInfo();

    UserResponse getUserById(String userId);

    UserResponse updateUserById(String userId, UserUpdateRequest userUpdateRequest);

    void deleteUserById(String userId);

    void logout(String token);
}
