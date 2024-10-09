package com.server.wordwaves.entity.user;

import com.server.wordwaves.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

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
    @JoinTable(name = "RoleToPermission",
            joinColumns = @JoinColumn(name = "RoleName", referencedColumnName = "name"),
    inverseJoinColumns = @JoinColumn(name = "PermissionName", referencedColumnName = "name"))
    Set<Permission> permissions;
}
