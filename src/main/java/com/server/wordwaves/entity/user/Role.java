package com.server.wordwaves.entity.user;

import java.util.Set;

import jakarta.persistence.*;

import com.server.wordwaves.entity.common.BaseEntity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Role extends BaseEntity {
    @Id
    String name;

    @ManyToMany
    @JoinTable(
            name = "RoleToPermission",
            joinColumns = @JoinColumn(name = "RoleName", referencedColumnName = "name"),
            inverseJoinColumns = @JoinColumn(name = "PermissionName", referencedColumnName = "name"))
    Set<Permission> permissions;
}
