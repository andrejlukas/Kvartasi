package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TrgovinaDTO {

    private Integer trgovinaId;

    @NotNull
    @Size(max = 50)
    private String trgovinaEmail;

    @NotNull
    @Size(max = 50)
    private String trgovinaNaziv;

    @NotNull
    @Size(max = 500)
    private String trgovinaOpis;

    @NotNull
    @Size(max = 50)
    private String trgovinaKategorija;

    @NotNull
    @Size(max = 50)
    private String trgovinaLokacija;

    @NotNull
    @Size(max = 50)
    private String trgovinaSlika;

    @NotNull
    @Size(max = 5)
    private String trgovinaRadnoVrijemeOd;
    
    @NotNull
    @Size(max = 5)
    private String trgovinaRadnoVrijemeDo;

    @NotNull
    @Size(max = 50)
    private String trgovinaSifra;


    private List<Integer> imaAtributeAtributs;

}
