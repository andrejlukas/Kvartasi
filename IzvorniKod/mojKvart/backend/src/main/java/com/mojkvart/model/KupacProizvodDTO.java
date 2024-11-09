package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KupacProizvodDTO {

    private Long id;

    @NotNull
    private Integer kolicinaProizvoda;

    private Long racun;

    @NotNull
    private Integer kupac;

    @NotNull
    private Integer proizvod;

}
