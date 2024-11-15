package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.OcjenaProizvodKupac;
import com.mojkvart.domain.Proizvod;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OcjenaProizvodKupacRepository extends JpaRepository<OcjenaProizvodKupac, Long> {

    OcjenaProizvodKupac findFirstByProizvod(Proizvod proizvod);

    OcjenaProizvodKupac findFirstByKupac(Kupac kupac);

}
