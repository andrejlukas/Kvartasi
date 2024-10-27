package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Trgovina;
import com.mojkvart.entities.Vlasnik;


public interface VlasnikRepository extends JpaRepository<Vlasnik, Integer> {

    Vlasnik findFirstByTrgovina(Trgovina trgovina);

}
