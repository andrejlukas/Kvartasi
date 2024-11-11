package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProizvodDTO {

    private Integer proizvodId;

    @NotNull
    @Size(max = 50)
    private String proizvodNaziv;

    @Size(max = 200)
    private String proizvodOpis;

    @NotNull
    private Integer proizvodCijena;

    @NotNull
    @Size(max = 50)
    private String proizvodKategorija;

    @NotNull
    @Size(max = 200)
    private String proizvodSlika;

    @NotNull
    private Boolean proizvodFlag;

    @NotNull
    private Integer trgovina;

}
