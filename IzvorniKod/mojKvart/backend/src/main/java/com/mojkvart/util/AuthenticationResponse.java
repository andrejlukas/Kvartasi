package com.mojkvart.util;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    //private String role;  ovo ce kasnije ici u token
}