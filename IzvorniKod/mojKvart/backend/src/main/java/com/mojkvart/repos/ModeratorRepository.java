package com.mojkvart.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mojkvart.entities.Moderator;


public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {
}
