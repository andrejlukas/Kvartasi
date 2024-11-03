package com.mojkvart.repos;

import com.mojkvart.domain.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
}
