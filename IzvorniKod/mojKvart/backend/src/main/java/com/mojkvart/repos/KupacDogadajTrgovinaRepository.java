package com.mojkvart.repos;

import com.mojkvart.domain.Dogadaj;
import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacDogadajTrgovina;
import com.mojkvart.domain.Trgovina;
import org.springframework.data.jpa.repository.JpaRepository;


public interface KupacDogadajTrgovinaRepository extends JpaRepository<KupacDogadajTrgovina, Long> {

    KupacDogadajTrgovina findFirstByKupac(Kupac kupac);

    KupacDogadajTrgovina findFirstByDogadaj(Dogadaj dogadaj);

    KupacDogadajTrgovina findFirstByTrgovina(Trgovina trgovina);

}
