package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KupacDogadajDTO {

    private Long id;

    @NotNull
    private Boolean kupacDogadajFlag;

    @NotNull
    private Integer kupac;

    @NotNull
    private Integer dogadaj;


}
