package com.server.wordwaves.dto.request.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionUpdateRequest {

    @Schema(example = "Cập nhật mô tả quyền hạn")
    String description;
}
