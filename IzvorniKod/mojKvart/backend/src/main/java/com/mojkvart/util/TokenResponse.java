package com.mojkvart.util;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

    @NotNull
    private String email;

    @NotNull
    private String role;
}