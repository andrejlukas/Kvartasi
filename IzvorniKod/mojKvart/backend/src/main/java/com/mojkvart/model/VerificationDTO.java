package com.mojkvart.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationDTO {
    @NotNull
    private String email;

    @NotNull
    private String verificationCode;

    @NotNull
    private Integer tries;
}