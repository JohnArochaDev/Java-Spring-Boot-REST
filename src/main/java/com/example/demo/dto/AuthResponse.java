package com.example.demo.dto;

public class AuthResponse {
    private String token;
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