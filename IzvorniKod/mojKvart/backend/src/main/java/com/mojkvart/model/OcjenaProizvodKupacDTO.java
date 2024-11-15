package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OcjenaProizvodKupacDTO {

    private Long id;

    @NotNull
    private Integer proizvod;

    @NotNull
    private Integer ocjena;

    @NotNull
    private Integer kupac;

}
