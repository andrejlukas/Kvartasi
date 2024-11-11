package com.mojkvart.repos;

import com.mojkvart.domain.Recenzija;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RecenzijaRepository extends JpaRepository<Recenzija, Integer> {

    // Metoda koja vraća sve recenzije za određeni trgovinaId
    List<Recenzija> findByTrgovina_TrgovinaId(Integer trgovinaId);
}
