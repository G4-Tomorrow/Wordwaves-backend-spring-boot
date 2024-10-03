package com.server.wordwaves.service;

import com.server.wordwaves.dto.response.EmailResponse;
import com.server.wordwaves.entity.User;

public interface EmailService {
    EmailResponse sendVerifyEmail(User user);
}
