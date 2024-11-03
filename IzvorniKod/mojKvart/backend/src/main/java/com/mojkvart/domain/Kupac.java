package com.mojkvart.domain;

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
public class Kupac {

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
    private Integer kupacId;

    @Column(nullable = false, length = 50)
    private String kupacEmail;

    @Column(nullable = false, length = 20)
    private String kupacIme;

    @Column(nullable = false, length = 20)
    private String kupacPrezime;

    @Column(nullable = false, length = 50)
    private String kupacAdresa;

    @Column(nullable = false, length = 50)
    private String kupacSifra;

    @OneToMany(mappedBy = "kupac")
    private Set<KupacDogadajTrgovina> kupacKupacDogadajTrgovinas;

    @OneToMany(mappedBy = "kupac")
    private Set<KupacTrgovinaRecenzija> kupacKupacTrgovinaRecenzijas;

    @OneToMany(mappedBy = "kupac")
    private Set<OcjenaProizvodKupac> kupacOcjenaProizvodKupacs;

    @OneToMany(mappedBy = "kupac")
    private Set<KupacTrgovinaPonudaPopust> kupacKupacTrgovinaPonudaPopusts;

    @OneToMany(mappedBy = "kupac")
    private Set<KupacProizvodTrgovina> kupacKupacProizvodTrgovinas;

}
