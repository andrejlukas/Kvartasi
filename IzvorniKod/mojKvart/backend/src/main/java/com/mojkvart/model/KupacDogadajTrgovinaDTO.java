package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KupacDogadajTrgovinaDTO {

    private Long id;

    @NotNull
    private Boolean kupacDogadajTrgovinaFlag;

    @NotNull
    private Integer kupac;

    @NotNull
    private Integer dogadaj;

    @NotNull
    private Integer trgovina;

}
