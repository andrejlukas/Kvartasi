package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class DogadajDTO {

    private Integer dogadajId;

    @Size(max = 500)
    private String dogadajOpis;

    @NotNull
    @Size(max = 500)
    private String dogadajNaziv;

    @NotNull
    @Size(max = 500)
    private String dogadajPocetak;

    @NotNull
    @Size(max = 500)
    private String dogadajKraj;

    @NotNull
    @Size(max = 500)
    private String dogadajSlika;

    @NotNull
    private Integer trgovina;

}
