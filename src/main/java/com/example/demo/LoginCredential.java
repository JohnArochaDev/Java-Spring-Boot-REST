package com.example.demo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.UUID;

@Entity
@Table(name = "login_credentials")
@Schema(description = "Login Credential entity representing a login for a user in the system")
public class LoginCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "UUID of the user", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Username of the credential", example = "user123")
    private String username;

    @Schema(description = "Password of the credential", example = "password123")
    private String password;

    @Schema(description = "Website of the credential", example = "password123")
    private String website;

    @ManyToOne
    @JsonBackReference
    private User user;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
