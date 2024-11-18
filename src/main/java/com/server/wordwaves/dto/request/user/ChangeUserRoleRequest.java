package com.server.wordwaves.dto.request.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeUserRoleRequest {

    @NotNull(message = "TYPE_IS_REQUIRED")
    @Pattern(regexp = "ADD|REMOVE", message = "INVALID_TYPE")
    @Schema(example = "ADD", description = "Loại hành động: ADD hoặc REMOVE")
    String type;

    @NotEmpty(message = "ROLE_NAMES_ARE_REQUIRED")
    @Schema(example = "[\"ADMIN\", \"HR\"]", description = "Danh sách các vai trò để thêm hoặc xóa")
    List<String> roleNames;
}
