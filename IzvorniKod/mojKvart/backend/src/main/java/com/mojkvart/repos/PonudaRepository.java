package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Ponuda;
import com.mojkvart.entities.PonudaPopust;


public interface PonudaRepository extends JpaRepository<Ponuda, Integer> {

    Ponuda findFirstByPonudaPopust(PonudaPopust ponudaPopust);

}
