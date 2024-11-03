package com.mojkvart.repos;

import com.mojkvart.domain.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {
}
