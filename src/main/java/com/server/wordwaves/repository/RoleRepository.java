package com.server.wordwaves.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.server.wordwaves.entity.user.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByName(String name);

    Page<Role> findByNameContainingOrDescriptionContaining(String name, String description, Pageable pageable);
}
