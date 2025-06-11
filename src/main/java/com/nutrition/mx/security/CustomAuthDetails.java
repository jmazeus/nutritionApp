package com.nutrition.mx.security;

public class CustomAuthDetails {
    private final String userId;
    private final String username;

    public CustomAuthDetails(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
