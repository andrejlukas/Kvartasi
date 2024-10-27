package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Korisnik;
import com.mojkvart.entities.KorisnikTrgovinaPonuda;
import com.mojkvart.entities.PonudaPopust;
import com.mojkvart.entities.Trgovina;


public interface KorisnikTrgovinaPonudaRepository extends JpaRepository<KorisnikTrgovinaPonuda, Long> {

    KorisnikTrgovinaPonuda findFirstByKorisnik(Korisnik korisnik);

    KorisnikTrgovinaPonuda findFirstByTrgovina(Trgovina trgovina);

    KorisnikTrgovinaPonuda findFirstByPonudaPopust(PonudaPopust ponudaPopust);

}
