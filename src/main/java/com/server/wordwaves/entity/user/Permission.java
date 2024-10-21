package com.server.wordwaves.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import com.server.wordwaves.entity.common.BaseEntity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Permission extends BaseEntity {
    @Id
    String name;

    String description;
}
