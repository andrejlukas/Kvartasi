package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PopustResponseDTO {
    private Integer popustId;
    private String popustNaziv;
    private String popustOpis;
    private Integer ponudaPopust;
    private String trgovinaIme;
    private Long kupacPonudaPopustId;

}
