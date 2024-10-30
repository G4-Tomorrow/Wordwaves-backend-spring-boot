package com.server.wordwaves.entity.user;

import java.util.Set;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Role extends BaseEntity {
    @Id
    String name;

    String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "UserToRole",
            joinColumns = @JoinColumn(name = "RoleName", referencedColumnName = "name"),
            inverseJoinColumns = @JoinColumn(name = "UserId", referencedColumnName = "id"))
    @JsonIgnore
    Set<User> users;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "RoleToPermission",
            joinColumns = @JoinColumn(name = "RoleName", referencedColumnName = "name"),
            inverseJoinColumns = @JoinColumn(name = "PermissionName", referencedColumnName = "name"))
    Set<Permission> permissions;
}
