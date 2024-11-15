package com.mojkvart.repos;

import com.mojkvart.domain.Dogadaj;
import com.mojkvart.domain.Trgovina;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DogadajRepository extends JpaRepository<Dogadaj, Integer> {

    Dogadaj findFirstByTrgovina(Trgovina trgovina);

}
