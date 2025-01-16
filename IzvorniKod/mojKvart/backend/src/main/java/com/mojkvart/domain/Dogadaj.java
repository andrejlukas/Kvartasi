package com.mojkvart.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Dogadaj {

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
    private Integer dogadajId;

    @Column(nullable = false, length = 500)
    private String dogadajNaziv;

    @Column(nullable = false, length = 50)
    private String dogadajPocetak;

    @Column(nullable = false, length = 50)
    private String dogadajKraj;

    @Column(nullable = false, length = 300)
    private String dogadajSlika;

    @Column(length = 500)
    private String dogadajOpis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trgovina_id", nullable = false)
    private Trgovina trgovina;

    @OneToMany(mappedBy = "dogadaj")
    private Set<KupacDogadaj> dogadajKupacDogadajs;

}
