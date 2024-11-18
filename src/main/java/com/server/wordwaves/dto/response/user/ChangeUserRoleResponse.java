package com.server.wordwaves.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeUserRoleResponse {
    @Schema(description = "Danh sách tất cả các vai trò hiện tại của người dùng sau khi thay đổi")
    List<String> currentRoles;

    @Schema(description = "Danh sách các vai trò đã được thêm")
    List<String> rolesAdded;

    @Schema(description = "Danh sách các vai trò đã bị xóa")
    List<String> rolesRemoved;
}
