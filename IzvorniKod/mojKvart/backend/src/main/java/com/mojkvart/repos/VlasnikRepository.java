package com.mojkvart.repos;

import com.mojkvart.domain.Trgovina;
import com.mojkvart.domain.Vlasnik;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VlasnikRepository extends JpaRepository<Vlasnik, Integer> {

    Vlasnik findFirstByTrgovina(Trgovina trgovina);

}
