package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class KupacDTO {

    private Integer kupacId;

    @NotNull
    @Size(max = 50)
    private String kupacEmail;

    @NotNull
    @Size(max = 50)
    private String kupacIme;

    @NotNull
    @Size(max = 50)
    private String kupacPrezime;

    @Size(max = 50)
    private String kupacAdresa;

    @Size(max = 200)
    private String kupacSifra;

    @Size(max = 200)
    private String verifikacijskiKod;

    private LocalDateTime kodValidanDo;

    @NotNull
    private String kupacStatus;
}