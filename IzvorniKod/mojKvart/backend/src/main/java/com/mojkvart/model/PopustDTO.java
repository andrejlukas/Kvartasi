package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class PopustDTO {

    private Integer popustId;

    @NotNull
    @Size(max = 50)
    private String popustQrkod;

    @NotNull
    @Size(max = 100)
    private String popustNaziv;

    @NotNull
    @Size(max = 200)
    private String popustOpis;

    @NotNull
    private String popustRok;

    @NotNull
    private Integer ponudaPopust;

    private String trgovinaIme;

}
