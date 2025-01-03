package com.example.demo.repository;

import com.example.demo.LoginCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LoginCredentialRepository extends JpaRepository<LoginCredential, UUID> {
}
