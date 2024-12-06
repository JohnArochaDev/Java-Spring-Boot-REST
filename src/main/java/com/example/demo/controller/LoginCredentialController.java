package com.example.demo.controller;

import com.example.demo.LoginCredential;
import com.example.demo.service.LoginCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/credentials")
public class LoginCredentialController {

    @Autowired
    private LoginCredentialService loginCredentialService;

    @GetMapping
    public List<LoginCredential> getAllLoginCredentials() throws Exception {
        return loginCredentialService.getAllLoginCredentials();
    }

    @GetMapping("/{id}")
    public LoginCredential getLoginCredentialById(@PathVariable UUID id) throws Exception {
        return loginCredentialService.getLoginCredentialById(id);
    }

    @PostMapping("/{userId}")
    public LoginCredential createLoginCredential(@PathVariable UUID userId, @RequestBody LoginCredential loginCredential) throws Exception {
        return loginCredentialService.createLoginCredential(userId, loginCredential);
    }

    @PutMapping("/{id}")
    public LoginCredential updateLoginCredential(@PathVariable UUID id, @RequestBody Map<String, Object> updates) throws Exception {
        LoginCredential loginCredentialDetails = new LoginCredential();
        if (updates.containsKey("username")) {
            loginCredentialDetails.setUsername((String) updates.get("username"));
        }
        if (updates.containsKey("password")) {
            loginCredentialDetails.setPassword((String) updates.get("password"));
        }
        if (updates.containsKey("website")) {
            loginCredentialDetails.setWebsite((String) updates.get("website"));
        }
        return loginCredentialService.updateLoginCredential(id, loginCredentialDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteLoginCredential(@PathVariable UUID id) throws Exception {
        loginCredentialService.deleteLoginCredentialById(id);
    }
}