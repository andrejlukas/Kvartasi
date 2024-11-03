package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacTrgovinaRecenzija;
import com.mojkvart.domain.Recenzija;
import com.mojkvart.domain.Trgovina;

import org.springframework.data.jpa.repository.JpaRepository;


public interface KupacTrgovinaRecenzijaRepository extends JpaRepository<KupacTrgovinaRecenzija, Long> {

    KupacTrgovinaRecenzija findFirstByKupac(Kupac kupac);

    KupacTrgovinaRecenzija findFirstByTrgovina(Trgovina trgovina);

    KupacTrgovinaRecenzija findFirstByRecenzija(Recenzija recenzija);

}
