package com.server.wordwaves.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_IS_REQUIRED")
    @Schema(example = "louisPham@yopmail.com")
    String email;

    //    @Size(min = 8, message = "INVALID_PASSWORD")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!$@%])[A-Za-z\\d!$@%]{8,}$",
            message = "WRONG_FORMAT_PASSWORD")
    @Schema(example = "12345@Qu")
    String password;
}
