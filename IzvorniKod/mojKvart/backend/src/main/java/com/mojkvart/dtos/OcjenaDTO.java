package com.mojkvart.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OcjenaDTO {

    private Integer ocjenaId;

    @NotNull
    private Integer ocjenaZvjezdice;

}
