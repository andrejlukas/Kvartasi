package com.mojkvart.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Recenzija {

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
    private Integer recenzijaId;

    @Column(length = 500)
    private String recenzijaOpis;

    @Column(nullable = false)
    private Integer recenzijaZvjezdice;

    @Column(length = 500)
    private String recenzijaOdgovor;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime vrijemeKreiranja;

    @OneToMany(mappedBy = "recenzija")
    private Set<KupacTrgovinaRecenzija> recenzijaKupacTrgovinaRecenzijas;

}
