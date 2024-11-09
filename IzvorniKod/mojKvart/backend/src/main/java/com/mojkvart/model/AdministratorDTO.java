package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdministratorDTO {

    private Integer administratorId;

    @NotNull
    @Size(max = 50)
    private String administratorIme;

    @NotNull
    @Size(max = 50)
    private String administratorPrezime;

    @NotNull
    @Size(max = 50)
    private String administratorEmail;

    @NotNull
    @Size(max = 200)
    private String administratorSifra;

}
