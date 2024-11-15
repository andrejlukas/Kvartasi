package com.mojkvart.repos;

import com.mojkvart.domain.Dogadaj;
import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacDogadaj;
import org.springframework.data.jpa.repository.JpaRepository;


public interface KupacDogadajRepository extends JpaRepository<KupacDogadaj, Long> {

    KupacDogadaj findFirstByKupac(Kupac kupac);

    KupacDogadaj findFirstByDogadaj(Dogadaj dogadaj);


}
