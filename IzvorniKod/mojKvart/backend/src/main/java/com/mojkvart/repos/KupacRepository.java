package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;

import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

import java.util.Optional;


public interface KupacRepository extends JpaRepository<Kupac, Integer> {
    Optional<Kupac> findByKupacEmail(String email);

    Optional<Kupac> findByKupacId(Integer kupacId);

    Streamable<Order> findByKupacStatus(String string);
}
