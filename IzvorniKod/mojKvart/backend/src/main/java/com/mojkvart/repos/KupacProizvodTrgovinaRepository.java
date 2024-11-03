package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacProizvodTrgovina;
import com.mojkvart.domain.Proizvod;
import com.mojkvart.domain.Trgovina;



import org.springframework.data.jpa.repository.JpaRepository;


public interface KupacProizvodTrgovinaRepository extends JpaRepository<KupacProizvodTrgovina, Long> {

    KupacProizvodTrgovina findFirstByKupac(Kupac kupac);

    KupacProizvodTrgovina findFirstByTrgovina(Trgovina trgovina);

    KupacProizvodTrgovina findFirstByProizvod(Proizvod proizvod);

}
