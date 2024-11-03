package com.server.wordwaves.dto.model.vocabulary;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordInLearningModel {
    String id;
    String name;
}
