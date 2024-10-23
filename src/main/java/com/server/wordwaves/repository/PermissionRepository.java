package com.server.wordwaves.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.server.wordwaves.entity.user.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {
    boolean existsByName(String name);

    Page<Permission> findByNameContainingOrDescriptionContaining(String name, String description, Pageable pageable);
}
