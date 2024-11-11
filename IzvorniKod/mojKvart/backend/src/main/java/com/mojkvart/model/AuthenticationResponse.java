package com.mojkvart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Integer id;
    private String role;

    @Override
    public String toString() {
        return "{'token': " + token + ", 'id': " + id + ", 'role': " + role + "}";
    }
}