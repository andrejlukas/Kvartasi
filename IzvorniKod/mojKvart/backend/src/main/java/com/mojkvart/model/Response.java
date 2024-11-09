package com.mojkvart.model;

public class Response {
    private String token;
    private String role;

    public Response(String token, String role) {
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
