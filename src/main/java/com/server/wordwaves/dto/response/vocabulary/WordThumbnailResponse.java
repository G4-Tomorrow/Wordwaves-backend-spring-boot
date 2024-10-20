package com.server.wordwaves.dto.response.vocabulary;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordThumbnailResponse {
    List<Photo> photos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Photo {
        Src src;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @FieldDefaults(level = AccessLevel.PRIVATE)
        public static class Src {
            String original;
        }
    }
}
