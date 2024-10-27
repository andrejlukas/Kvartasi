package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.PonudaPopust;
import com.mojkvart.entities.Popust;


public interface PopustRepository extends JpaRepository<Popust, Integer> {

    Popust findFirstByPonudaPopust(PonudaPopust ponudaPopust);

}
