package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @NotNull
    @Size(max = 50)
    private String email;

    @NotNull
    @Size(max = 200)
    private String sifra;
}
