package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.OcjenaProizvodKupac;
import com.mojkvart.domain.Proizvod;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface OcjenaProizvodKupacRepository extends JpaRepository<OcjenaProizvodKupac, Long> {

    OcjenaProizvodKupac findFirstByProizvod(Proizvod proizvod);
    List<OcjenaProizvodKupac> findAllByProizvod_ProizvodId(Integer proizvodId);
    OcjenaProizvodKupac findFirstByKupac(Kupac kupac);

}
