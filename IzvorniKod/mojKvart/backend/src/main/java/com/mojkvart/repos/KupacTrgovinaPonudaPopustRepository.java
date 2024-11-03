package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacTrgovinaPonudaPopust;
import com.mojkvart.domain.PonudaPopust;
import com.mojkvart.domain.Trgovina;
import org.springframework.data.jpa.repository.JpaRepository;


public interface KupacTrgovinaPonudaPopustRepository extends JpaRepository<KupacTrgovinaPonudaPopust, Long> {

    KupacTrgovinaPonudaPopust findFirstByKupac(Kupac kupac);

    KupacTrgovinaPonudaPopust findFirstByTrgovina(Trgovina trgovina);

    KupacTrgovinaPonudaPopust findFirstByPonudaPopust(PonudaPopust ponudaPopust);

}
