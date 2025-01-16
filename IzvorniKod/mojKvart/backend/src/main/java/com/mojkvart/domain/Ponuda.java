package com.mojkvart.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
public class Ponuda {

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
    private Integer ponudaId;

    @Column(nullable = false, length = 100)
    private String ponudaNaziv;

    @Column(nullable = false, length = 200)
    private String ponudaOpis;

    @Column(nullable = false)
    private LocalDateTime ponudaRok;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponuda_popust_id", nullable = false)
    private PonudaPopust ponudaPopust;

    @Transient // Ovo polje nije mapirano u bazu
    private String trgovinaIme;

}
