package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacProizvod;
import com.mojkvart.domain.Proizvod;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface KupacProizvodRepository extends JpaRepository<KupacProizvod, Long> {

    KupacProizvod findFirstByKupac(Kupac kupac);
    List<KupacProizvod> findByKupac_KupacId(Integer kupacId);
    List<KupacProizvod> findByRacun_Trgovina_TrgovinaId(Integer trgovinaId);
    KupacProizvod findFirstByProizvod(Proizvod proizvod);

}
