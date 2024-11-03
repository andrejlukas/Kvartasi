package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KupacProizvodTrgovinaDTO {

    private Long id;

    @NotNull
    private Boolean kupacProizvodTrgovinaFlag;

    @NotNull
    private Integer kupac;

    @NotNull
    private Integer trgovina;

    @NotNull
    private Integer proizvod;

}
