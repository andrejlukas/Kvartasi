package com.mojkvart.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OcjenaProizvodKorisnikDTO {

    private Long id;

    @NotNull
    private Integer proizvod;

    @NotNull
    private Integer ocjena;

    @NotNull
    private Integer korisnik;

}
