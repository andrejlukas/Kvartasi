package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RacunDTO {

    private Long racunId;

    private LocalDateTime vrijemeDatumNastanka;

    @NotNull
    private Character stanje;

    @NotNull
    private Integer kupac;

    @NotNull
    private Integer trgovina;
}