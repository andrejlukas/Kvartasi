package com.mojkvart.repos;

import com.mojkvart.domain.Racun;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RacunRepository extends JpaRepository<Racun, Long> {

    
        Optional<Racun> findByKupac_KupacIdAndTrgovina_TrgovinaIdAndStanje(Integer kupacId, Integer trgovinaId, Character stanje);
    }