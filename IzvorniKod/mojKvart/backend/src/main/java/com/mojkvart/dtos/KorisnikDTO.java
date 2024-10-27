package com.mojkvart.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KorisnikDTO {

    private Integer korisnikId;

    @NotNull
    @Size(max = 50)
    private String korisnikEmail;

    @NotNull
    @Size(max = 20)
    private String korisnikIme;

    @NotNull
    @Size(max = 20)
    private String korisnikPrezime;

    @NotNull
    @Size(max = 50)
    private String korisnikAdresa;

    @NotNull
    @Size(max = 50)
    private String korisnikSifra;

}
