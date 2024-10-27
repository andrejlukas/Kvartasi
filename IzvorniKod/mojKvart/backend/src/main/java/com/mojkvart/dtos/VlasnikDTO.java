package com.mojkvart.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VlasnikDTO {

    private Integer vlasnikId;

    @NotNull
    @Size(max = 50)
    private String vlasnikIme;

    @NotNull
    @Size(max = 50)
    private String vlasnikPrezime;

    @NotNull
    @Size(max = 50)
    private String vlasnikEmail;

    @NotNull
    @Size(max = 50)
    private String vlasnikSifra;

    private Integer trgovina;

}
