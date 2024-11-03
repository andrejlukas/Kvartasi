package com.mojkvart.repos;

import com.mojkvart.domain.Proizvod;
import com.mojkvart.domain.Trgovina;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProizvodRepository extends JpaRepository<Proizvod, Integer> {

    Proizvod findFirstByTrgovina(Trgovina trgovina);

}
