package com.mojkvart.repos;

import com.mojkvart.domain.PonudaPopust;
import com.mojkvart.domain.Popust;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PopustRepository extends JpaRepository<Popust, Integer> {

    Popust findFirstByPonudaPopust(PonudaPopust ponudaPopust);

}
