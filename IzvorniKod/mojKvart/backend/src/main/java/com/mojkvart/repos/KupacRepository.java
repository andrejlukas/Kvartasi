package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;
import org.springframework.data.jpa.repository.JpaRepository;


public interface KupacRepository extends JpaRepository<Kupac, Integer> {
}
