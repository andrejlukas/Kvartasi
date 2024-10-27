package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Proizvod;


public interface ProizvodRepository extends JpaRepository<Proizvod, Integer> {
}
