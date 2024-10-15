package com.server.wordwaves.dto.request.vocabulary;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicAddWordsRequest {
    @Schema(
            example = "[\"hello\", \"an\", \"word\"]"
    )
    List<String> wordIds;
}
