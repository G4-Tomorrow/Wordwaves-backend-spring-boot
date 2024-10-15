package com.server.wordwaves.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    @NotBlank(message = "EMAIL_IS_REQUIRED")
    @Schema(example = "quan01@yopmail.com")
    String email;

    @NotBlank(message = "INVALID_PASSWORD")
    @Schema(example = "12345@Qu")
    String password;
}
