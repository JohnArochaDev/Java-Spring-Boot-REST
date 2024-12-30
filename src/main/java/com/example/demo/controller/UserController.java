package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.User;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Get all users", description = "Retrieve a list of all users for development")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public List<User> getAllUsers() throws Exception {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) throws Exception {
        return userService.getUserById(id);
    }

    @Operation(summary = "Register a new user", description = "Create a new user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "409", description = "Conflict - User already exists", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) throws Exception {
        User createdUser = userService.createUser(user);
        if (createdUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }
    }

    @Operation(summary = "Update user details", description = "Update the details of an existing user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User userDetails) throws Exception {
        return userService.updateUser(id, userDetails);
    }

    @Operation(summary = "Delete user", description = "Delete a user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @Operation(summary = "User login", description = "Authenticate a user and generate a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid email or password", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            boolean isAuthenticated = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            if (isAuthenticated) {
                UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
                String token = jwtService.generateToken(userDetails);

                User user = userService.findByEmail(loginRequest.getUsername());
                String userId = user.getId().toString();

                return ResponseEntity.ok(new AuthResponse(token, userId));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}