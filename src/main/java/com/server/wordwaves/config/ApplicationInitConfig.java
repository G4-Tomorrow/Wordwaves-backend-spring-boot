package com.server.wordwaves.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.server.wordwaves.constant.PredefinedRole;
import com.server.wordwaves.entity.Role;
import com.server.wordwaves.entity.User;
import com.server.wordwaves.repository.RoleRepository;
import com.server.wordwaves.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    @NonFinal
    static final String ADMIN_EMAIL = "wordwaves.admin@yopmail.com";

    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        log.info("Init application");

        return args -> {
            if (!userRepository.existsByEmail(ADMIN_EMAIL)) {
                roleRepository.save(
                        Role.builder().name(PredefinedRole.USER_ROLE.getName()).build());

                Role adminRole = roleRepository.save(
                        Role.builder().name(PredefinedRole.ADMIN_ROLE.getName()).build());

                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);

                User user = User.builder()
                        .email(ADMIN_EMAIL)
                        .roles(roles)
                        .password(passwordEncoder.encode("admin"))
                        .build();

                userRepository.save(user);
                log.info("init completed");
            }
        };
    }
}
