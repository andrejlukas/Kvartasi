package com.mojkvart.repos;

import com.mojkvart.domain.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

    Optional<Administrator> findByAdministratorEmail(String email);
}
