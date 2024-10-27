package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Korisnik;
import com.mojkvart.entities.KorisnikTrgovinaRecenzija;
import com.mojkvart.entities.Recenzija;
import com.mojkvart.entities.Trgovina;


public interface KorisnikTrgovinaRecenzijaRepository extends JpaRepository<KorisnikTrgovinaRecenzija, Long> {

    KorisnikTrgovinaRecenzija findFirstByKorisnik(Korisnik korisnik);

    KorisnikTrgovinaRecenzija findFirstByTrgovina(Trgovina trgovina);

    KorisnikTrgovinaRecenzija findFirstByRecenzija(Recenzija recenzija);

}
