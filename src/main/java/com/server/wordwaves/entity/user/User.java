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

    @Column(unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String email;

    String password;
    String fullName;
    String avatarName;

    @ManyToMany
    @JoinTable(name = "UserToRole")
    Set<Role> roles;

    @Column(columnDefinition = "TEXT")
    String refreshToken;
}
