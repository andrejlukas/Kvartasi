package com.mojkvart.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KorisnikDogadajTrgovinaDTO {

    private Long id;

    @NotNull
    private Integer korisnik;

    @NotNull
    private Integer dogadaj;

    @NotNull
    private Integer trgovina;

}
