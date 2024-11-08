package com.mojkvart.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Racun {
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
    private Long racunId;

    @Column(nullable = false)
    private LocalDateTime vrijemeDatumNastanka;

    @Column(nullable = false)
    private boolean placen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kupac_id", nullable = false)
    private Kupac kupac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trgovina_id", nullable = false)
    private Trgovina trgovina;
}