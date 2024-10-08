package com.server.wordwaves.dto.model.word;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordModel {
    String word;
    List<PhoneticModel> phonetics;
    List<MeaningModel> meanings;
}
