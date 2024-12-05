package com.example.demo.controller;

import com.example.demo.LoginCredential;
import com.example.demo.User;
import com.example.demo.repository.LoginCredentialRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/credentials")
public class LoginCredentialController {

    @Autowired
    private LoginCredentialRepository loginCredentialRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<LoginCredential> getAllLoginCredentials() {
        return loginCredentialRepository.findAll();
    }

    @GetMapping("/{id}")
    public LoginCredential getLoginCredentialById(@PathVariable Long id) {
        return loginCredentialRepository.findById(id).orElseThrow(() -> new RuntimeException("Credential not found"));
    }

    @PostMapping
    public LoginCredential createLoginCredential(@RequestBody LoginCredential loginCredential) {
        return loginCredentialRepository.save(loginCredential);
    }

    @PutMapping("/{id}")
    public LoginCredential updateLoginCredential(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        LoginCredential loginCredential = loginCredentialRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Credential not found"));

        if (updates.containsKey("username")) {
            loginCredential.setUsername((String) updates.get("username"));
        }
        if (updates.containsKey("password")) {
            loginCredential.setPassword((String) updates.get("password"));
        }
        if (updates.containsKey("website")) {
            loginCredential.setWebsite((String) updates.get("website"));
        }
        if (updates.containsKey("userId")) {
            Long userId = ((Number) updates.get("userId")).longValue();
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            loginCredential.setUser(user);
        }

        return loginCredentialRepository.save(loginCredential);
    }

    @DeleteMapping("/{id}")
    public void deleteLoginCredential(@PathVariable Long id) {
        LoginCredential loginCredential = loginCredentialRepository.findById(id).orElseThrow(() -> new RuntimeException("Credential not found"));
        loginCredentialRepository.delete(loginCredential);
    }
}