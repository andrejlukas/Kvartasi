package com.mojkvart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Trgovina {

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
    private Integer trgovinaId;

    @Column(nullable = false, length = 50)
    private String trgovinaNaziv;

    @Column(nullable = false, length = 500)
    private String trgovinaOpis;

    @Column(nullable = false, length = 50)
    private String trgovinaLokacija;

    @Column(nullable = false, length = 50)
    private String trgovinaSlika;

    @Column(nullable = false, length = 50)
    private String trgovinaAtributi;

    @OneToMany(mappedBy = "trgovina")
    private Set<Vlasnik> trgovinaVlasniks;

    @OneToMany(mappedBy = "trgovina")
    private Set<KorisnikDogadajTrgovina> trgovinaKorisnikDogadajTrgovinas;

    @OneToMany(mappedBy = "trgovina")
    private Set<KorisnikTrgovinaRecenzija> trgovinaKorisnikTrgovinaRecenzijas;

    @OneToMany(mappedBy = "trgovina")
    private Set<KorisnikTrgovinaPonuda> trgovinaKorisnikTrgovinaPonudas;

    @ManyToMany
    @JoinTable(
            name = "ImaAtribute",
            joinColumns = @JoinColumn(name = "trgovinaId"),
            inverseJoinColumns = @JoinColumn(name = "atributId")
    )
    private Set<Atribut> imaAtributeAtributs;

}
