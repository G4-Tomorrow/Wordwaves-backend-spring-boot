package com.server.wordwaves.dto.model.vocabulary;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneticModel {
    String text;
    String audio;
}
