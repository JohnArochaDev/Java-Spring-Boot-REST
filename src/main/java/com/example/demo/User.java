package com.example.demo;

import com.example.demo.roles.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Schema(description = "User entity representing a user in the system")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "UUID of the user", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @Column(unique = true)
    @Schema(description = "Email of the user", example = "johndoe@gmail.com")
    private String username;

    @Schema(description = "Password of the user", example = "password123")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Roles assigned to the user (Not currently in service)")
    private Set<Role> authorities;

    @Schema(description = "Indicates whether the user's account is expired", example = "true")
    private boolean accountNonExpired = true;

    @Schema(description = "Indicates whether the user's account is enabled(Not currently in service)", example = "true")
    private boolean isEnabled = true;

    @Schema(description = "Indicates whether the user's account is locked(Not currently in service)", example = "true")
    private boolean accountNonLocked = true;

    @Schema(description = "Indicates whether the user's JWT are expired", example = "true")
    private boolean credentialsNonExpired = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Schema(description = "List of login credentials associated with the user")
    private List<LoginCredential> loginCredentials = new ArrayList<>();

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<LoginCredential> getLoginCredentials() {
        return this.loginCredentials;
    }

    public void setLoginCredentials(List<LoginCredential> loginCredentials) {
        this.loginCredentials = loginCredentials;
    }

    @Override
    public Collection<Role> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}