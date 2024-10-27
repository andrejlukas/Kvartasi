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
public class Korisnik {

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
    private Integer korisnikId;

    @Column(nullable = false, length = 50)
    private String korisnikEmail;

    @Column(nullable = false, length = 20)
    private String korisnikIme;

    @Column(nullable = false, length = 20)
    private String korisnikPrezime;

    @Column(nullable = false, length = 50)
    private String korisnikAdresa;

    @Column(nullable = false, length = 50)
    private String korisnikSifra;

    @OneToMany(mappedBy = "korisnik")
    private Set<KorisnikDogadajTrgovina> korisnikKorisnikDogadajTrgovinas;

    @OneToMany(mappedBy = "korisnik")
    private Set<KorisnikTrgovinaRecenzija> korisnikKorisnikTrgovinaRecenzijas;

    @OneToMany(mappedBy = "korisnik")
    private Set<OcjenaProizvodKorisnik> korisnikOcjenaProizvodKorisniks;

    @OneToMany(mappedBy = "korisnik")
    private Set<KorisnikTrgovinaPonuda> korisnikKorisnikTrgovinaPonudas;

}
