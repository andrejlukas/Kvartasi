package com.mojkvart.repos;

import com.mojkvart.domain.Proizvod;
import com.mojkvart.domain.Trgovina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProizvodRepository extends JpaRepository<Proizvod, Integer> {

    Proizvod findFirstByTrgovina(Trgovina trgovina);

    @Query("SELECT p FROM Proizvod p WHERE p.trgovina.trgovinaId = :trgovinaId AND p.proizvodFlag = 'A'")
    List<Proizvod> findAllApprovedByTrgovinaId(@Param("trgovinaId") Integer trgovinaId);

    @Query("SELECT p FROM Proizvod p WHERE p.trgovina.trgovinaId = :trgovinaId AND p.proizvodFlag = 'N'")
    List<Proizvod> findAllNotApprovedByTrgovinaId(@Param("trgovinaId") Integer trgovinaId);

    @Query("SELECT p FROM Proizvod p WHERE p.trgovina.trgovinaId = :trgovinaId AND p.proizvodFlag = 'R'")
    List<Proizvod> findAllRejectedByTrgovinaId(@Param("trgovinaId") Integer trgovinaId);

    @Query("SELECT p FROM Proizvod p WHERE p.proizvodFlag = 'A'")
    List<Proizvod> findAllApproved();

    @Query("SELECT p FROM Proizvod p WHERE p.proizvodFlag = 'N'")
    List<Proizvod> findAllNotApproved();

}