package com.mojkvart.repos;

import com.mojkvart.domain.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {

    Optional<Moderator> findByModeratorEmail(String email);
}
