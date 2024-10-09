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
public class Topic extends BaseAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;
    String thumbnailName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TopicToWord",
            joinColumns = @JoinColumn(name = "TopicId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "WordId", referencedColumnName = "id"))
    Set<Word> words;
}
