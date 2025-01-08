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
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Transient;



@Entity
@Getter
@Setter
public class Popust {

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
    private Integer popustId;

    @Column(nullable = false, length = 50)
    private String popustQrkod;

    @Column(nullable = false, length = 100)
    private String popustNaziv;

    @Column(nullable = false, length = 200)
    private String popustOpis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponuda_popust_id", nullable = false)
    private PonudaPopust ponudaPopust;

    @Transient
    private String trgovinaIme;

}
