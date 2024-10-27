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
public class PonudaPopust {

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
    private Integer ponudaPopustId;

    @OneToMany(mappedBy = "ponudaPopust")
    private Set<Ponuda> ponudaPopustPonudas;

    @OneToMany(mappedBy = "ponudaPopust")
    private Set<Popust> ponudaPopustPopusts;

    @OneToMany(mappedBy = "ponudaPopust")
    private Set<KorisnikTrgovinaPonuda> ponudaPopustKorisnikTrgovinaPonudas;

}
