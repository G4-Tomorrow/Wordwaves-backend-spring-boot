package com.server.wordwaves.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.wordwaves.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndRefreshToken(String id, String refreshToken);

    Page<User> findByFullNameContainingOrEmailContaining(String searchQuery, String searchQuery1, Pageable pageable);
}
