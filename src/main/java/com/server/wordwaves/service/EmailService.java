package com.server.wordwaves.service;

import com.server.wordwaves.dto.response.common.EmailResponse;
import com.server.wordwaves.entity.user.User;

public interface EmailService {
    EmailResponse sendVerifyEmail(User user);

    EmailResponse sendForgotPasswordEmail(User user);

    String generateEmailVerifyToken(User user);
}
