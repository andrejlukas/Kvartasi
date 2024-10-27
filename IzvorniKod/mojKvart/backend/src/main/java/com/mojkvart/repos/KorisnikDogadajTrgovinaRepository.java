package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Dogadaj;
import com.mojkvart.entities.Korisnik;
import com.mojkvart.entities.KorisnikDogadajTrgovina;
import com.mojkvart.entities.Trgovina;


public interface KorisnikDogadajTrgovinaRepository extends JpaRepository<KorisnikDogadajTrgovina, Long> {

    KorisnikDogadajTrgovina findFirstByKorisnik(Korisnik korisnik);

    KorisnikDogadajTrgovina findFirstByDogadaj(Dogadaj dogadaj);

    KorisnikDogadajTrgovina findFirstByTrgovina(Trgovina trgovina);

}
