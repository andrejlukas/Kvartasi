package com.mojkvart.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KorisnikTrgovinaPonudaDTO {

    private Long id;

    @NotNull
    private Integer korisnik;

    @NotNull
    private Integer trgovina;

    @NotNull
    private Integer ponudaPopust;

}
