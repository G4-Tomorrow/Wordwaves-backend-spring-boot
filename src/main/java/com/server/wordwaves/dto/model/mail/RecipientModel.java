package com.server.wordwaves.dto.model.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipientModel {
    //    String name;
    @Schema(example = "quan01@yopmail.com")
    String email;
}
