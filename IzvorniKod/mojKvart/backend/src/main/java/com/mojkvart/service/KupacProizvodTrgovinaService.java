package com.mojkvart.service;

import com.mojkvart.domain.*;
import com.mojkvart.model.KupacProizvodTrgovinaDTO;
import com.mojkvart.repos.*;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class KupacProizvodTrgovinaService {

    private final KupacProizvodTrgovinaRepository kupacProizvodTrgovinaRepository;
    private final RacunRepository racunRepository;
    private final KupacRepository kupacRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final ProizvodRepository proizvodRepository;

    public KupacProizvodTrgovinaService(
            final KupacProizvodTrgovinaRepository kupacProizvodTrgovinaRepository,
            final RacunRepository racunRepository,
            final KupacRepository kupacRepository,
            final TrgovinaRepository trgovinaRepository,
            final ProizvodRepository proizvodRepository) {
        this.kupacProizvodTrgovinaRepository = kupacProizvodTrgovinaRepository;
        this.racunRepository = racunRepository;
        this.kupacRepository = kupacRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.proizvodRepository = proizvodRepository;
    }

    public List<KupacProizvodTrgovinaDTO> findAll() {
        final List<KupacProizvodTrgovina> kupacProizvodTrgovinas = kupacProizvodTrgovinaRepository.findAll(Sort.by("id"));
        return kupacProizvodTrgovinas.stream()
                .map(kupacProizvodTrgovina -> mapToDTO(kupacProizvodTrgovina, new KupacProizvodTrgovinaDTO()))
                .toList();
    }

    public KupacProizvodTrgovinaDTO get(final Long id) {
        return kupacProizvodTrgovinaRepository.findById(id)
                .map(kupacProizvodTrgovina -> mapToDTO(kupacProizvodTrgovina, new KupacProizvodTrgovinaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final KupacProizvodTrgovinaDTO kupacProizvodTrgovinaDTO) {
        final KupacProizvodTrgovina kupacProizvodTrgovina = new KupacProizvodTrgovina();
        mapToEntity(kupacProizvodTrgovinaDTO, kupacProizvodTrgovina);
        return kupacProizvodTrgovinaRepository.save(kupacProizvodTrgovina).getId();
    }

    
    public void update(final Long id, final KupacProizvodTrgovinaDTO kupacProizvodTrgovinaDTO) {
        final KupacProizvodTrgovina kupacProizvodTrgovina = kupacProizvodTrgovinaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(kupacProizvodTrgovinaDTO, kupacProizvodTrgovina);
        kupacProizvodTrgovinaRepository.save(kupacProizvodTrgovina);
    }

    public void delete(final Long id) {
        kupacProizvodTrgovinaRepository.deleteById(id);
    }

    private KupacProizvodTrgovinaDTO mapToDTO(final KupacProizvodTrgovina kupacProizvodTrgovina,
            final KupacProizvodTrgovinaDTO kupacProizvodTrgovinaDTO) {
        kupacProizvodTrgovinaDTO.setId(kupacProizvodTrgovina.getId());
        kupacProizvodTrgovinaDTO.setKolicinaProizvoda(kupacProizvodTrgovina.getKolicinaProizvoda());
        kupacProizvodTrgovinaDTO.setRacun(kupacProizvodTrgovina.getRacun() == null ? null : kupacProizvodTrgovina.getRacun().getRacunId());
        kupacProizvodTrgovinaDTO.setKupac(kupacProizvodTrgovina.getKupac() == null ? null : kupacProizvodTrgovina.getKupac().getKupacId());
        kupacProizvodTrgovinaDTO.setTrgovina(kupacProizvodTrgovina.getTrgovina() == null ? null : kupacProizvodTrgovina.getTrgovina().getTrgovinaId());
        kupacProizvodTrgovinaDTO.setProizvod(kupacProizvodTrgovina.getProizvod() == null ? null : kupacProizvodTrgovina.getProizvod().getProizvodId());
        return kupacProizvodTrgovinaDTO;
    }

    private KupacProizvodTrgovina mapToEntity(
            final KupacProizvodTrgovinaDTO kupacProizvodTrgovinaDTO,
            final KupacProizvodTrgovina kupacProizvodTrgovina) {
        kupacProizvodTrgovina.setKolicinaProizvoda(kupacProizvodTrgovinaDTO.getKolicinaProizvoda());
        final Racun racun = kupacProizvodTrgovinaDTO.getRacun() == null ? null : racunRepository.findById(kupacProizvodTrgovinaDTO.getRacun())
                .orElseThrow(() -> new NotFoundException("racun not found"));
        kupacProizvodTrgovina.setRacun(racun);
        final Kupac kupac = kupacProizvodTrgovinaDTO.getKupac() == null ? null : kupacRepository.findById(kupacProizvodTrgovinaDTO.getKupac())
                .orElseThrow(() -> new NotFoundException("kupac not found"));
        kupacProizvodTrgovina.setKupac(kupac);
        final Trgovina trgovina = kupacProizvodTrgovinaDTO.getTrgovina() == null ? null : trgovinaRepository.findById(kupacProizvodTrgovinaDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        kupacProizvodTrgovina.setTrgovina(trgovina);
        final Proizvod proizvod = kupacProizvodTrgovinaDTO.getProizvod() == null ? null : proizvodRepository.findById(kupacProizvodTrgovinaDTO.getProizvod())
                .orElseThrow(() -> new NotFoundException("proizvod not found"));
        kupacProizvodTrgovina.setProizvod(proizvod);
        return kupacProizvodTrgovina;
    }

}
