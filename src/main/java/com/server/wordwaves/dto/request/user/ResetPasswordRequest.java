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
    @NotBlank(message = "Mật khẩu mới không được để trống.")
    String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống.")
    String confirmPassword;
}
