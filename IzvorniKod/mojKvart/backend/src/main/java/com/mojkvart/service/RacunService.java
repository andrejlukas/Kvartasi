package com.mojkvart.service;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.Racun;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.RacunDTO;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.repos.RacunRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RacunService {
    private final RacunRepository racunRepository;
    private final KupacRepository kupacRepository;
    private final TrgovinaRepository trgovinaRepository;

    public RacunService(final RacunRepository racunRepository, final KupacRepository kupacRepository, final TrgovinaRepository trgovinaRepository) {
        this.racunRepository = racunRepository;
        this.kupacRepository = kupacRepository;
        this.trgovinaRepository = trgovinaRepository;
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

    public void delete(final Long racunId) {
        racunRepository.deleteById(racunId);
    }

    private RacunDTO mapToDTO(
            final Racun racun,
            final RacunDTO racunDTO) {
        racunDTO.setRacunId(racun.getRacunId());
        racunDTO.setVrijemeDatumNastanka(racun.getVrijemeDatumNastanka());
        racunDTO.setPlacen(racun.isPlacen());
        racunDTO.setKupac(racun.getKupac() == null ? null : racun.getKupac().getKupacId());
        racunDTO.setTrgovina(racun.getTrgovina() == null ? null : racun.getTrgovina().getTrgovinaId());
        return racunDTO;
    }

    private Racun mapToEntity(
            final RacunDTO racunDTO,
            final Racun racun) {
        racun.setVrijemeDatumNastanka(racunDTO.getVrijemeDatumNastanka());
        racun.setPlacen(racunDTO.isPlacen());
        final Kupac kupac = racunDTO.getKupac() == null ? null : kupacRepository.findById(racunDTO.getKupac())
                .orElseThrow(() -> new NotFoundException("kupac not found"));
        racun.setKupac(kupac);
        final Trgovina trgovina = racunDTO.getTrgovina() == null ? null : trgovinaRepository.findById(racunDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        racun.setTrgovina(trgovina);
        return racun;
    }
}