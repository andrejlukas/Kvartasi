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
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Proizvod {

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
    private Integer proizvodId;

    @Column(nullable = false, length = 50)
    private String proizvodNaziv;

    @Column(length = 200)
    private String proizvodOpis;

    @Column(nullable = false)
    private Integer proizvodCijena;

    @Column(nullable = false, length = 50)
    private String proizvodKategorija;

    @Column(nullable = false, length = 50)
    private String proizvodSlika;

    @Column(nullable = false)
    private Boolean proizvodFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trgovina_id", nullable = false)
    private Trgovina trgovina;

    @OneToMany(mappedBy = "proizvod")
    private Set<OcjenaProizvodKupac> proizvodOcjenaProizvodKupacs;

    @OneToMany(mappedBy = "proizvod")
    private Set<KupacProizvod> proizvodKupacProizvods;

}
