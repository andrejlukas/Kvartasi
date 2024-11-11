package com.mojkvart.model;

public class AutentificationResponse {
    private String token;
    private String role;

    public AutentificationResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "{'token': " + token + ", 'role': " + role + "}";
    }
}
