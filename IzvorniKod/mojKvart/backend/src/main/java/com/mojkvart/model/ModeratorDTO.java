package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ModeratorDTO {

    private Integer moderatorId;

    @NotNull
    @Size(max = 50)
    private String moderatorIme;

    @NotNull
    @Size(max = 50)
    private String moderatorPrezime;

    @NotNull
    @Size(max = 50)
    private String moderatorEmail;

    @NotNull
    @Size(max = 50)
    private String moderatorSifra;

}
