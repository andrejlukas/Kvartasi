package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Administrator;


public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
}
