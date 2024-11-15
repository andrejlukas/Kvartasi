package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KupacPonudaPopustDTO {

    private Long id;

    @NotNull
    private Boolean kupacPonudaPopustFlag;

    @NotNull
    private Integer kupac;

    @NotNull
    private Integer trgovina;

    @NotNull
    private Integer ponudaPopust;

}
