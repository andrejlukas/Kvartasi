package com.mojkvart.service;

import com.mojkvart.domain.*;
import com.mojkvart.model.KupacProizvodDTO;
import com.mojkvart.model.KupacProizvodInfoDTO;
import com.mojkvart.repos.*;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


@Service
public class KupacProizvodService {

    private final KupacProizvodRepository kupacProizvodRepository;
    private final RacunRepository racunRepository;
    private final KupacRepository kupacRepository;
    private final ProizvodRepository proizvodRepository;

    public KupacProizvodService(
            final KupacProizvodRepository kupacProizvodRepository,
            final RacunRepository racunRepository,
            final KupacRepository kupacRepository,
            final TrgovinaRepository trgovinaRepository,
            final ProizvodRepository proizvodRepository) {
        this.kupacProizvodRepository = kupacProizvodRepository;
        this.racunRepository = racunRepository;
        this.kupacRepository = kupacRepository;
        this.proizvodRepository = proizvodRepository;
    }

    public Map<Long, List<KupacProizvodInfoDTO>> getKupacKosarica(Integer kupacId) {
        // Dohvati sve KupacProizvod zapise za kupca
        List<KupacProizvod> kupacProizvodi = kupacProizvodRepository.findByKupac_KupacId(kupacId);
    
        // Grupiraj proizvode po trgovinama koristeći mapu
        return kupacProizvodi.stream().filter(kp -> kp.getRacun().getStanje() == 'K')
                .collect(Collectors.groupingBy(
                        kp -> kp.getRacun().getRacunId(), // Grupiraj po racun id-ju
                        Collectors.mapping(kp -> new KupacProizvodInfoDTO(kp.getProizvod().getProizvodNaziv(), kp.getProizvod().getProizvodCijena(),
                        kp.getProizvod().getProizvodSlika(), kp.getKolicinaProizvoda(), kp.getRacun().getTrgovina().getTrgovinaNaziv()), // Mapiraj id i količinu proizvoda
                                Collectors.toList()) // Prikupljaj proizvode u listu
                ));
    }

    public Map<Long, List<KupacProizvodInfoDTO>> getKupacProsleNarudzbe(Integer kupacId) {
        // Dohvati sve KupacProizvod zapise za kupca
        List<KupacProizvod> kupacProizvodi = kupacProizvodRepository.findByKupac_KupacId(kupacId);
    
        // Grupiraj proizvode po trgovinama koristeći mapu
        return kupacProizvodi.stream().filter(kp -> kp.getRacun().getStanje() == 'P')
                .collect(Collectors.groupingBy(
                        kp -> kp.getRacun().getRacunId(), // Grupiraj po racun Id
                        Collectors.mapping(kp -> new KupacProizvodInfoDTO(kp.getProizvod().getProizvodNaziv(), kp.getProizvod().getProizvodCijena(),
                        kp.getProizvod().getProizvodSlika(), kp.getKolicinaProizvoda(), kp.getRacun().getTrgovina().getTrgovinaNaziv()), // Mapiraj id i količinu proizvoda
                                Collectors.toList()) // Prikupljaj proizvode u listu
                ));
    }

    public Map<String, List<KupacProizvodInfoDTO>> getTrgovinaNarudzbe(Integer trgovinaId) {
        // Dohvati sve KupacProizvod zapise za trgovinu
        List<KupacProizvod> kupacProizvodi = kupacProizvodRepository.findByRacun_Trgovina_TrgovinaId(trgovinaId);
    
        // Grupiraj proizvode po kupcima koristeći mapu
        return kupacProizvodi.stream().filter(kp -> kp.getRacun().getStanje() == 'T')
                .collect(Collectors.groupingBy(
                        kp -> kp.getRacun().getKupac().getKupacEmail(), // Grupiraj po emailu kupca
                        Collectors.mapping(kp -> new KupacProizvodInfoDTO(kp.getProizvod().getProizvodNaziv(), kp.getProizvod().getProizvodCijena(),
                        kp.getProizvod().getProizvodSlika(), kp.getKolicinaProizvoda(), kp.getRacun().getTrgovina().getTrgovinaNaziv()), // Mapiraj id i količinu proizvoda
                                Collectors.toList()) // Prikupljaj proizvode u listu
                ));
    }






    public KupacProizvodDTO get(final Long id) {
        return kupacProizvodRepository.findById(id)
                .map(kupacProizvod -> mapToDTO(kupacProizvod, new KupacProizvodDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final KupacProizvodDTO kupacProizvodDTO) {
        final KupacProizvod kupacProizvod = new KupacProizvod();
        mapToEntity(kupacProizvodDTO, kupacProizvod);
        return kupacProizvodRepository.save(kupacProizvod).getId();
    }

    
    public void update(final Long id, final KupacProizvodDTO kupacProizvodDTO) {
        final KupacProizvod kupacProizvod = kupacProizvodRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(kupacProizvodDTO, kupacProizvod);
        kupacProizvodRepository.save(kupacProizvod);
    }

    public void delete(final Long id) {
        kupacProizvodRepository.deleteById(id);
    }

    private KupacProizvodDTO mapToDTO(final KupacProizvod kupacProizvod,
            final KupacProizvodDTO kupacProizvodDTO) {
        kupacProizvodDTO.setId(kupacProizvod.getId());
        kupacProizvodDTO.setKolicinaProizvoda(kupacProizvod.getKolicinaProizvoda());
        kupacProizvodDTO.setRacun(kupacProizvod.getRacun() == null ? null : kupacProizvod.getRacun().getRacunId());
        kupacProizvodDTO.setKupac(kupacProizvod.getKupac() == null ? null : kupacProizvod.getKupac().getKupacId());
        kupacProizvodDTO.setProizvod(kupacProizvod.getProizvod() == null ? null : kupacProizvod.getProizvod().getProizvodId());
        return kupacProizvodDTO;
    }

    private KupacProizvod mapToEntity(
            final KupacProizvodDTO kupacProizvodDTO,
            final KupacProizvod kupacProizvod) {
        kupacProizvod.setKolicinaProizvoda(kupacProizvodDTO.getKolicinaProizvoda());
        final Racun racun = kupacProizvodDTO.getRacun() == null ? null : racunRepository.findById(kupacProizvodDTO.getRacun())
                .orElseThrow(() -> new NotFoundException("racun not found"));
        kupacProizvod.setRacun(racun);
        final Kupac kupac = kupacProizvodDTO.getKupac() == null ? null : kupacRepository.findById(kupacProizvodDTO.getKupac())
                .orElseThrow(() -> new NotFoundException("kupac not found"));
        kupacProizvod.setKupac(kupac);
        final Proizvod proizvod = kupacProizvodDTO.getProizvod() == null ? null : proizvodRepository.findById(kupacProizvodDTO.getProizvod())
                .orElseThrow(() -> new NotFoundException("proizvod not found"));
        kupacProizvod.setProizvod(proizvod);
        return kupacProizvod;
    }

}
