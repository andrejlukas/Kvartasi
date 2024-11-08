package com.mojkvart.repos;

import com.mojkvart.domain.Racun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RacunRepository extends JpaRepository<Racun, Long> {
}