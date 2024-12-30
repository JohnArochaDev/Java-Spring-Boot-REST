package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login Response dto")
public class AuthResponse {

    @Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiJ9.hbkdoN93kSIAHJIADhsaaod")
    private String token;

    @Schema(description = "JWT token", example = "123e4567-e89b-12d3-a456-426614174000")
    private String userId;

    public AuthResponse(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }
}