package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KupacTrgovinaRecenzijaDTO {

    private Long id;

    @NotNull
    private Integer kupac;

    @NotNull
    private Integer trgovina;

    @NotNull
    private Integer recenzija;

}
