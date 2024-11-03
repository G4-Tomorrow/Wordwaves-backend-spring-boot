package com.server.wordwaves.dto.request.permission;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionCreationRequest {

    @NotBlank(message = "PERMISSION_NAME_IS_REQUIRED")
    @Schema(example = "READ_USER")
    String name;

    @Schema(example = "Cho phép đọc thông tin người dùng")
    String description;
}
