package com.mojkvart.service;

import com.mojkvart.domain.Dogadaj;
import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacDogadajTrgovina;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.KupacDogadajTrgovinaDTO;
import com.mojkvart.repos.DogadajRepository;
import com.mojkvart.repos.KupacDogadajTrgovinaRepository;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class KupacDogadajTrgovinaService {

    private final KupacDogadajTrgovinaRepository kupacDogadajTrgovinaRepository;
    private final KupacRepository kupacRepository;
    private final DogadajRepository dogadajRepository;
    private final TrgovinaRepository trgovinaRepository;

    public KupacDogadajTrgovinaService(
            final KupacDogadajTrgovinaRepository kupacDogadajTrgovinaRepository,
            final KupacRepository kupacRepository, final DogadajRepository dogadajRepository,
            final TrgovinaRepository trgovinaRepository) {
        this.kupacDogadajTrgovinaRepository = kupacDogadajTrgovinaRepository;
        this.kupacRepository = kupacRepository;
        this.dogadajRepository = dogadajRepository;
        this.trgovinaRepository = trgovinaRepository;
    }

    public List<KupacDogadajTrgovinaDTO> findAll() {
        final List<KupacDogadajTrgovina> kupacDogadajTrgovinas = kupacDogadajTrgovinaRepository.findAll(Sort.by("id"));
        return kupacDogadajTrgovinas.stream()
                .map(kupacDogadajTrgovina -> mapToDTO(kupacDogadajTrgovina, new KupacDogadajTrgovinaDTO()))
                .toList();
    }

    public KupacDogadajTrgovinaDTO get(final Long id) {
        return kupacDogadajTrgovinaRepository.findById(id)
                .map(kupacDogadajTrgovina -> mapToDTO(kupacDogadajTrgovina, new KupacDogadajTrgovinaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final KupacDogadajTrgovinaDTO kupacDogadajTrgovinaDTO) {
        final KupacDogadajTrgovina kupacDogadajTrgovina = new KupacDogadajTrgovina();
        mapToEntity(kupacDogadajTrgovinaDTO, kupacDogadajTrgovina);
        return kupacDogadajTrgovinaRepository.save(kupacDogadajTrgovina).getId();
    }

    public void update(final Long id, final KupacDogadajTrgovinaDTO kupacDogadajTrgovinaDTO) {
        final KupacDogadajTrgovina kupacDogadajTrgovina = kupacDogadajTrgovinaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(kupacDogadajTrgovinaDTO, kupacDogadajTrgovina);
        kupacDogadajTrgovinaRepository.save(kupacDogadajTrgovina);
    }

    public void delete(final Long id) {
        kupacDogadajTrgovinaRepository.deleteById(id);
    }

    private KupacDogadajTrgovinaDTO mapToDTO(final KupacDogadajTrgovina kupacDogadajTrgovina,
            final KupacDogadajTrgovinaDTO kupacDogadajTrgovinaDTO) {
        kupacDogadajTrgovinaDTO.setId(kupacDogadajTrgovina.getId());
        kupacDogadajTrgovinaDTO.setKupacDogadajTrgovinaFlag(kupacDogadajTrgovina.getKupacDogadajTrgovinaFlag());
        kupacDogadajTrgovinaDTO.setKupac(kupacDogadajTrgovina.getKupac() == null ? null : kupacDogadajTrgovina.getKupac().getKupacId());
        kupacDogadajTrgovinaDTO.setDogadaj(kupacDogadajTrgovina.getDogadaj() == null ? null : kupacDogadajTrgovina.getDogadaj().getDogadajId());
        kupacDogadajTrgovinaDTO.setTrgovina(kupacDogadajTrgovina.getTrgovina() == null ? null : kupacDogadajTrgovina.getTrgovina().getTrgovinaId());
        return kupacDogadajTrgovinaDTO;
    }

    private KupacDogadajTrgovina mapToEntity(final KupacDogadajTrgovinaDTO kupacDogadajTrgovinaDTO,
            final KupacDogadajTrgovina kupacDogadajTrgovina) {
        kupacDogadajTrgovina.setKupacDogadajTrgovinaFlag(kupacDogadajTrgovinaDTO.getKupacDogadajTrgovinaFlag());
        final Kupac kupac = kupacDogadajTrgovinaDTO.getKupac() == null ? null : kupacRepository.findById(kupacDogadajTrgovinaDTO.getKupac())
                .orElseThrow(() -> new NotFoundException("kupac not found"));
        kupacDogadajTrgovina.setKupac(kupac);
        final Dogadaj dogadaj = kupacDogadajTrgovinaDTO.getDogadaj() == null ? null : dogadajRepository.findById(kupacDogadajTrgovinaDTO.getDogadaj())
                .orElseThrow(() -> new NotFoundException("dogadaj not found"));
        kupacDogadajTrgovina.setDogadaj(dogadaj);
        final Trgovina trgovina = kupacDogadajTrgovinaDTO.getTrgovina() == null ? null : trgovinaRepository.findById(kupacDogadajTrgovinaDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        kupacDogadajTrgovina.setTrgovina(trgovina);
        return kupacDogadajTrgovina;
    }

}
