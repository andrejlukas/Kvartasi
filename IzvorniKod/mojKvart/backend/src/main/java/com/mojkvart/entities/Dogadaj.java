package com.mojkvart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
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

    @Column(length = 50)
    private String dogadajOpis;

    @Column(nullable = false, length = 50)
    private String dogadajNaziv;

    @Column(nullable = false, length = 50)
    private String dogadajVrijeme;

    @Column(nullable = false, length = 50)
    private String dogadajSlika;

    @OneToMany(mappedBy = "dogadaj")
    private Set<KorisnikDogadajTrgovina> dogadajKorisnikDogadajTrgovinas;

}
