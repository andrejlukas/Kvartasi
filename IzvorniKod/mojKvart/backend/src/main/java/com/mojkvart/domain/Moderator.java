package com.mojkvart.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Moderator {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer moderatorId;

    @Column(nullable = false, length = 50)
    private String moderatorIme;

    @Column(nullable = false, length = 50)
    private String moderatorPrezime;

    @Column(nullable = false, length = 50)
    private String moderatorEmail;

    @Column(nullable = false, length = 50)
    private String moderatorSifra;

}
