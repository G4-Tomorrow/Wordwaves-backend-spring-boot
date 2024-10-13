package com.server.wordwaves.dto.request.user;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {
    @NotBlank(message = "EMPTY_TOKEN")
    String token;

    @NotBlank(message = "NEW_PASSWORD_IS_REQUIRED")
    String newPassword;

    @NotBlank(message = "CONFIRM_PASSWORD_IS_REQUIRED")
    String confirmPassword;
}
