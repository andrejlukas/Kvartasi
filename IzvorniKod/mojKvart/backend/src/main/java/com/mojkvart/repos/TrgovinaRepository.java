package com.mojkvart.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Atribut;
import com.mojkvart.entities.Trgovina;


public interface TrgovinaRepository extends JpaRepository<Trgovina, Integer> {

    Trgovina findFirstByImaAtributeAtributs(Atribut atribut);

    List<Trgovina> findAllByImaAtributeAtributs(Atribut atribut);

}
