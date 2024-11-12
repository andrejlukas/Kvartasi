package com.mojkvart.util;

import lombok.*;

@Getter
@Setter
@Builder
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