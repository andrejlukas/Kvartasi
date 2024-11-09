package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacProizvod;
import com.mojkvart.domain.Proizvod;



import org.springframework.data.jpa.repository.JpaRepository;


public interface KupacProizvodRepository extends JpaRepository<KupacProizvod, Long> {

    KupacProizvod findFirstByKupac(Kupac kupac);

    KupacProizvod findFirstByProizvod(Proizvod proizvod);

}
