package com.server.wordwaves.entity.user;

import java.util.Set;

import jakarta.persistence.*;

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
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String email;

    String password;

    @Column(columnDefinition = "TEXT")
    String refreshToken;

    String fullName;

    String phoneNumber;

    String avatarName;

    String provider;

    String providerUserId;

    @ManyToMany(mappedBy = "users", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<Role> roles;
}
