package com.server.wordwaves.dto.request.vocabulary;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicAddWordsRequest {
    List<String> wordIds;
}
