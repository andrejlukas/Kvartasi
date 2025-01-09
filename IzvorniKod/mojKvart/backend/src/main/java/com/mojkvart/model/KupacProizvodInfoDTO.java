package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class KupacProizvodInfoDTO {
    @NotNull
    private String proizvodNaziv;

    @NotNull
    private Double proizvodCijena;

    @NotNull
    private String proizvodSlika;

    @NotNull
    private Integer proizvodKolicina;

    @NotNull
    private String trgovinaNaziv;

}
