package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Recenzija;


public interface RecenzijaRepository extends JpaRepository<Recenzija, Integer> {
}
