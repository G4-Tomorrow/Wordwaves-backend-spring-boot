package com.server.wordwaves.service;

import com.server.wordwaves.dto.request.UserCreationRequest;
import com.server.wordwaves.dto.response.EmailResponse;

public interface UserService {
    EmailResponse register(UserCreationRequest request);

    String verify(String token);
}
