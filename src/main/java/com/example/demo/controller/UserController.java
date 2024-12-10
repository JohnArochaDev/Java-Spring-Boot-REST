package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.User;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @GetMapping
    public List<User> getAllUsers() throws Exception {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) throws Exception {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) throws Exception {
        User createdUser = userService.createUser(user);
        if (createdUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }
    }


    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User userDetails) throws Exception {
        return userService.updateUser(id, userDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("TRY TO LOGINNNN");
        try {
            System.out.println("Attempting to authenticate user with email: " + loginRequest.getEmail());
            boolean isAuthenticated = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
            System.out.println("Authentication result: " + isAuthenticated);
            if (isAuthenticated) {
                UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
                String token = jwtService.generateToken(userDetails);
                LOGGER.log(Level.INFO, "Generated JWT token for user: {0}", userDetails.getUsername());
                System.out.println("Generated JWT token for user: " + userDetails.getUsername());
                return ResponseEntity.ok(new AuthResponse(token));
            } else {
                LOGGER.log(Level.WARNING, "Invalid email or password for user: {0}", loginRequest.getEmail());
                System.out.println("Invalid email or password for user: " + loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred during login", e);
            System.out.println("An error occurred during login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}