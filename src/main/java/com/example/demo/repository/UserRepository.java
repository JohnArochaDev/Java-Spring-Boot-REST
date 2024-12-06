package com.example.demo.repository;

import com.example.demo.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = "loginCredentials")
    Optional<User> findById(UUID id);
    Optional<User> findByEmailIgnoreCase(String email);
}