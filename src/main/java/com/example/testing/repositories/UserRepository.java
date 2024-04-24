package com.example.testing.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.testing.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
