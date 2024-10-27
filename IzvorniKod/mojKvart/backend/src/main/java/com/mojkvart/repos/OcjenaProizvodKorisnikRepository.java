package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Korisnik;
import com.mojkvart.entities.Ocjena;
import com.mojkvart.entities.OcjenaProizvodKorisnik;
import com.mojkvart.entities.Proizvod;


public interface OcjenaProizvodKorisnikRepository extends JpaRepository<OcjenaProizvodKorisnik, Long> {

    OcjenaProizvodKorisnik findFirstByProizvod(Proizvod proizvod);

    OcjenaProizvodKorisnik findFirstByOcjena(Ocjena ocjena);

    OcjenaProizvodKorisnik findFirstByKorisnik(Korisnik korisnik);

}
