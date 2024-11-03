package com.mojkvart.repos;

import com.mojkvart.domain.Recenzija;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecenzijaRepository extends JpaRepository<Recenzija, Integer> {
}
