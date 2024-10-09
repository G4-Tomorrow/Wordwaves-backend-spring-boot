package com.server.wordwaves.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.user.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
