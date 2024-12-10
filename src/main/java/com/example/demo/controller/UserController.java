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

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

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
        try {
            boolean isAuthenticated = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
            if (isAuthenticated) {
                UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
                String token = jwtService.generateToken(userDetails);
                return ResponseEntity.ok(new AuthResponse(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}