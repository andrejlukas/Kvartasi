package com.mojkvart.repos;

import com.mojkvart.domain.Atribut;
import com.mojkvart.domain.Trgovina;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TrgovinaRepository extends JpaRepository<Trgovina, Integer> {

    Trgovina findFirstByImaAtributeAtributs(Atribut atribut);

    List<Trgovina> findAllByImaAtributeAtributs(Atribut atribut);

    Optional<Trgovina> findByTrgovinaEmail(String email);
}
