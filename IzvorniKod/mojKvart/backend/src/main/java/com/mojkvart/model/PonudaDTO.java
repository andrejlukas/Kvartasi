package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class PonudaDTO {

    private Integer ponudaId;

    @NotNull
    @Size(max = 100)
    private String ponudaNaziv;

    @NotNull
    @Size(max = 200)
    private String ponudaOpis;

    @NotNull
    private LocalDateTime ponudaRok;

    @NotNull
    private Integer ponudaPopust;

    // nije obavezno polje, njega popunjavam s trgovinaIme prilikom fetcha
    private String trgovinaIme;
}
