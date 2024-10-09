package com.server.wordwaves.entity.user;

import java.util.Set;

import jakarta.persistence.*;

import com.server.wordwaves.entity.common.BaseEntity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String email;

    String password;
    String fullName;
    String avatarName;

    @ManyToMany
    @JoinTable(name = "users_roles")
    Set<Role> roles;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    String refreshToken;
}
