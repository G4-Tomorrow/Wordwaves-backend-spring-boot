package com.server.wordwaves.entity.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import com.server.wordwaves.entity.common.BaseEntity;

import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;


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

    @ManyToMany(mappedBy = "permissions", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<Role> roles;
}
