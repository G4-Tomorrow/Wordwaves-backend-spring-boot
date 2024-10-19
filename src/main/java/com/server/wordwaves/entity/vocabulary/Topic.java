package com.server.wordwaves.entity.vocabulary;

import java.util.Set;

import jakarta.persistence.*;

import com.server.wordwaves.entity.common.BaseAuthor;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class Topic extends BaseAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;
    String thumbnailName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "TopicToWord",
            joinColumns = @JoinColumn(name = "TopicId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "WordId", referencedColumnName = "id"))
    Set<Word> words;
}
