package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface KupacRepository extends JpaRepository<Kupac, Integer> {
    Optional<Kupac> findByKupacEmail(String email);

    Optional<Kupac> findByKupacId(Integer kupacId);
}
