package com.mojkvart.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OcjenaProizvodKupacDTO {

    private Long id;

    @NotNull
    private Integer proizvod;

    @Min(1)
    @Max(5)
    @NotNull
    private Integer ocjena;

    @NotNull
    private Integer kupac;

}
