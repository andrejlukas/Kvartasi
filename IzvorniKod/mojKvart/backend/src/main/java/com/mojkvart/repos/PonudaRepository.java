package com.mojkvart.repos;

import com.mojkvart.domain.Ponuda;
import com.mojkvart.domain.PonudaPopust;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PonudaRepository extends JpaRepository<Ponuda, Integer> {

    Ponuda findFirstByPonudaPopust(PonudaPopust ponudaPopust);

}
