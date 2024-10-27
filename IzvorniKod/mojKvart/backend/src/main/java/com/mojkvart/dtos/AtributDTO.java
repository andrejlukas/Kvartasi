package com.mojkvart.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AtributDTO {

    private Integer atributId;

    @NotNull
    @Size(max = 200)
    private String atributOpis;

}
