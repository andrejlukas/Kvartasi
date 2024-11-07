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


@Entity
@Getter
@Setter
public class KupacTrgovinaPonudaPopust {

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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kupac_id", nullable = false)
    private Kupac kupac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trgovina_id", nullable = false)
    private Trgovina trgovina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponuda_popust_id", nullable = false)
    private PonudaPopust ponudaPopust;

}
