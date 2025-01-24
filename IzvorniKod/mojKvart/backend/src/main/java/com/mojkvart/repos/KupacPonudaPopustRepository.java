package com.mojkvart.repos;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacPonudaPopust;
import com.mojkvart.domain.PonudaPopust;
import org.springframework.data.jpa.repository.JpaRepository;


public interface KupacPonudaPopustRepository extends JpaRepository<KupacPonudaPopust, Long> {

    KupacPonudaPopust findFirstByKupac(Kupac kupac);
    KupacPonudaPopust findById(Integer id);
    KupacPonudaPopust findFirstByPonudaPopust(PonudaPopust ponudaPopust);

}
