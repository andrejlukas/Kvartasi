package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PonudaResponseDTO {
    private Integer ponudaId;
    private String ponudaNaziv;
    private String ponudaOpis;
    private Integer ponudaPopust;
    private String trgovinaIme;
    private Long kupacPonudaPopustId;
}
