package com.server.wordwaves.dto.request.role;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreationRequest {

    @NotBlank(message = "ROLE_NAME_IS_REQUIRED")
    @Schema(example = "ADMIN")
    String name;

    @Schema(example = "Vai trò quản trị viên với đầy đủ quyền hạn")
    String description;
}
