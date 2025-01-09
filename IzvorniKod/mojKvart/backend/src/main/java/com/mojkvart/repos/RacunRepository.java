package com.mojkvart.repos;

import com.mojkvart.domain.Racun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RacunRepository extends JpaRepository<Racun, Long> {

    Racun findByKupac_KupacIdAndTrgovina_TrgovinaIdAndStanje(Integer kupacId, Integer trgovinaId, char c);
}