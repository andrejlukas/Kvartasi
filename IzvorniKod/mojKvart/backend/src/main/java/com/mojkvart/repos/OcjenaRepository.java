package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Ocjena;


public interface OcjenaRepository extends JpaRepository<Ocjena, Integer> {
}
