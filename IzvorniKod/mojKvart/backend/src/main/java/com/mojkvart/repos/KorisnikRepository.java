package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Korisnik;


public interface KorisnikRepository extends JpaRepository<Korisnik, Integer> {
}
