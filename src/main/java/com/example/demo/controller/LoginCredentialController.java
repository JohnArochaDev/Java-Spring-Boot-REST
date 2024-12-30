package com.example.demo.controller;

import com.example.demo.LoginCredential;
import com.example.demo.service.LoginCredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/credentials")
@Tag(name = "Login Credential Management", description = "Operations related to login credential management")
public class LoginCredentialController {

    @Autowired
    private LoginCredentialService loginCredentialService;

    @Operation(summary = "Get all login credentials", description = "Retrieve a list of all login credentials for development")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of login credentials"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public List<LoginCredential> getAllLoginCredentials() throws Exception {
        return loginCredentialService.getAllLoginCredentials();
    }

    @Operation(summary = "Get login credential by ID", description = "Retrieve a login credential by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved login credential"),
        @ApiResponse(responseCode = "404", description = "Login credential not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    public LoginCredential getLoginCredentialById(@PathVariable UUID id) throws Exception {
        return loginCredentialService.getLoginCredentialById(id);
    }

    @Operation(summary = "Create a new login credential", description = "Create a new login credential for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Login credential created successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/{userId}")
    public LoginCredential createLoginCredential(@PathVariable UUID userId, @RequestBody LoginCredential loginCredential) throws Exception {
        return loginCredentialService.createLoginCredential(userId, loginCredential);
    }

    @Operation(summary = "Update login credential", description = "Update the details of an existing login credential")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login credential updated successfully"),
        @ApiResponse(responseCode = "404", description = "Login credential not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
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

    @Operation(summary = "Delete login credential", description = "Delete a login credential by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Login credential deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Login credential not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public void deleteLoginCredential(@PathVariable UUID id) throws Exception {
        loginCredentialService.deleteLoginCredentialById(id);
    }
}