package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Dogadaj;


public interface DogadajRepository extends JpaRepository<Dogadaj, Integer> {
}
