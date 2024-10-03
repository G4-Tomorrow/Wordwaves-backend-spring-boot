package com.server.wordwaves.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
