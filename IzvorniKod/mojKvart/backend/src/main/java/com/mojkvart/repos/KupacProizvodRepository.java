package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacProizvod;
import com.mojkvart.domain.Proizvod;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface KupacProizvodRepository extends JpaRepository<KupacProizvod, Long> {

    KupacProizvod findFirstByKupac(Kupac kupac);
    List<KupacProizvod> findByKupac_KupacId(Integer kupacId);
    List<KupacProizvod> findByRacun_Trgovina_TrgovinaId(Integer trgovinaId);
    KupacProizvod findFirstByProizvod(Proizvod proizvod);
    Optional<KupacProizvod> findByKupac_KupacIdAndProizvod_ProizvodId(Long kupacId, Long proizvodId);
    Optional<KupacProizvod> findByRacun_RacunIdAndProizvod_ProizvodId(Long racunId, Integer proizvodId);

}
