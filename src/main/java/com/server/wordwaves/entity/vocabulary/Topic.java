package com.server.wordwaves.entity.vocabulary;

import com.server.wordwaves.entity.common.BaseAuthor;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "topic")
public class Topic extends BaseAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;
    String thumbnailName;

    @ManyToMany
    @JoinTable(name = "topics_words")
    Set<Word> words;
}
