package com.server.wordwaves.entity;

import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "email", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String email;

    String password;
    String fullName;

    //    @Column(name = "email_verified", nullable = false, columnDefinition = "boolean default false")
    //    boolean emailVerified = false;

    @ManyToMany
    @JoinTable(name = "user_roles")
    Set<Role> roles;
}
