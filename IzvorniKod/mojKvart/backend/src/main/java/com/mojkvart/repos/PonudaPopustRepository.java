package com.mojkvart.repos;

import com.mojkvart.domain.PonudaPopust;
import com.mojkvart.domain.Trgovina;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PonudaPopustRepository extends JpaRepository<PonudaPopust, Integer> {

    PonudaPopust findFirstByTrgovina(Trgovina trgovina);

}
