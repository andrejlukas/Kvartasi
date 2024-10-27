package com.mojkvart.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PopustDTO {

    private Integer popustId;

    @NotNull
    @Size(max = 50)
    private String popustQrkod;

    @NotNull
    @Size(max = 200)
    private String popustOpis;

    @NotNull
    private Integer ponudaPopust;

}
