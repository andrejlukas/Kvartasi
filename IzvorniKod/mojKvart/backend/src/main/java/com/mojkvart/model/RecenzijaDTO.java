package com.mojkvart.model;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RecenzijaDTO {

    private Integer recenzijaId;

    @Size(max = 500)
    private String recenzijaOpis;

    @NotNull
    private Integer recenzijaZvjezdice;

    @Size(max = 500)
    private String recenzijaOdgovor;

    private LocalDateTime vrijemeKreiranja;

}
