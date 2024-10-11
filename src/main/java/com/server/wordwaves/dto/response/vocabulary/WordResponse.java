package com.server.wordwaves.dto.response.vocabulary;

import java.util.List;

import com.server.wordwaves.dto.model.vocabulary.MeaningModel;
import com.server.wordwaves.dto.model.vocabulary.PhoneticModel;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordResponse {
    String word;
    List<PhoneticModel> phonetics;
    List<MeaningModel> meanings;
}
