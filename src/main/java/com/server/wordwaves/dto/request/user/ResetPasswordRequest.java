package com.server.wordwaves.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "12345@Cu")

    String newPassword;

    @NotBlank(message = "CONFIRM_PASSWORD_IS_REQUIRED")
    @Schema(example = "12345@Cu")
    String confirmPassword;
}
