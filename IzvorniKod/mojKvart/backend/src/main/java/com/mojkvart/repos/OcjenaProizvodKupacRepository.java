package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.Ocjena;
import com.mojkvart.domain.OcjenaProizvodKupac;
import com.mojkvart.domain.Proizvod;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OcjenaProizvodKupacRepository extends JpaRepository<OcjenaProizvodKupac, Long> {

    OcjenaProizvodKupac findFirstByProizvod(Proizvod proizvod);

    OcjenaProizvodKupac findFirstByOcjena(Ocjena ocjena);

    OcjenaProizvodKupac findFirstByKupac(Kupac kupac);

}
