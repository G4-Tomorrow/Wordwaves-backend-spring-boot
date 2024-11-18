package com.server.wordwaves.dto.model.vocabulary;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordsTranslateModel {
    String platform;

    @Builder.Default
    String from = "en_GB";

    @Builder.Default
    String to = "vi_VN";

    List<String> data;
}
