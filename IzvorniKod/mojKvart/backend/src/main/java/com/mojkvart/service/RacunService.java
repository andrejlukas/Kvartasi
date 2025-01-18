package com.mojkvart.service;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacProizvod;
import com.mojkvart.domain.Racun;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.RacunDTO;
import com.mojkvart.repos.KupacProizvodRepository;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.repos.RacunRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class RacunService {
    private final RacunRepository racunRepository;
    private final KupacRepository kupacRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final KupacProizvodRepository kupacProizvodRepository;

    public RacunService(final RacunRepository racunRepository, final KupacRepository kupacRepository, final TrgovinaRepository trgovinaRepository,
    final KupacProizvodRepository kupacProizvodRepository) {
        this.racunRepository = racunRepository;
        this.kupacRepository = kupacRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.kupacProizvodRepository = kupacProizvodRepository;
    }

    public List<RacunDTO> findAll() {
        final List<Racun> racuns = racunRepository.findAll(Sort.by("racunId"));
        return racuns.stream()
                .map(racun -> mapToDTO(racun, new RacunDTO()))
                .toList();
    }

    public RacunDTO get(final Long racunId) {
        return racunRepository.findById(racunId)
                .map(racun -> mapToDTO(racun, new RacunDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RacunDTO racunDTO) {
        final Racun racun = new Racun();
        mapToEntity(racunDTO, racun);
        return racunRepository.save(racun).getRacunId();
    }

    public void update(final Long racunId,
            final RacunDTO racunDTO) {
        final Racun racun = racunRepository.findById(racunId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(racunDTO, racun);
        racunRepository.save(racun);
    }

    public void promijeniStanje(Long racunId, Character novoStanje) {
        Racun racun = racunRepository.findById(racunId)
                .orElseThrow(() -> new NotFoundException("Račun nije pronađen: " + racunId));
        if (!isValidStanje(novoStanje)) {
            throw new IllegalArgumentException("Neispravno stanje: " + novoStanje);
        }
        racun.setStanje(novoStanje);
        racunRepository.save(racun);
    }

    private boolean isValidStanje(Character stanje) {
        return stanje == 'K' || stanje == 'T' || stanje == 'P';
    }

    public void delete(final Long racunId) {
        racunRepository.deleteById(racunId);
    }


    public Long getRacunForKupacAndTrgovina(Integer kupacId, Integer trgovinaId) {
        Optional<Racun> racunOptional = racunRepository.findByKupac_KupacIdAndTrgovina_TrgovinaIdAndStanje(kupacId, trgovinaId, 'K');
    
        if (racunOptional.isPresent()) {
            return racunOptional.get().getRacunId();
        } else {
            Kupac kupac = kupacRepository.findById(kupacId)
                    .orElseThrow(() -> new NotFoundException("Kupac nije pronađen"));
    
            Trgovina trgovina = trgovinaRepository.findById(trgovinaId)
                    .orElseThrow(() -> new NotFoundException("Trgovina nije pronađena"));
    
            RacunDTO racunDTO = new RacunDTO();
            racunDTO.setStanje('K');
            racunDTO.setKupac(kupac.getKupacId());
            racunDTO.setTrgovina(trgovina.getTrgovinaId());
    
            Long racunId = create(racunDTO);
    
            return racunId;
        }
    }
    

    private RacunDTO mapToDTO(
            final Racun racun,
            final RacunDTO racunDTO) {
        racunDTO.setRacunId(racun.getRacunId());
        racunDTO.setVrijemeDatumNastanka(racun.getVrijemeDatumNastanka());
        racunDTO.setStanje(racun.getStanje());
        racunDTO.setKupac(racun.getKupac() == null ? null : racun.getKupac().getKupacId());
        racunDTO.setTrgovina(racun.getTrgovina() == null ? null : racun.getTrgovina().getTrgovinaId());
        return racunDTO;
    }

    private Racun mapToEntity(
            final RacunDTO racunDTO,
            final Racun racun) {
        racun.setVrijemeDatumNastanka(racunDTO.getVrijemeDatumNastanka());
        racun.setStanje(racunDTO.getStanje());
        final Kupac kupac = racunDTO.getKupac() == null ? null : kupacRepository.findById(racunDTO.getKupac())
                .orElseThrow(() -> new NotFoundException("kupac not found"));
        racun.setKupac(kupac);
        final Trgovina trgovina = racunDTO.getTrgovina() == null ? null : trgovinaRepository.findById(racunDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        racun.setTrgovina(trgovina);
        return racun;
    }
}