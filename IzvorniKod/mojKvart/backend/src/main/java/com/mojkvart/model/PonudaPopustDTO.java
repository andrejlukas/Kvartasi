package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PonudaPopustDTO {

    private Integer ponudaPopustId;

    @NotNull
    private Boolean ponudaPopustFlag;

    @NotNull
    private Integer trgovina;

}
