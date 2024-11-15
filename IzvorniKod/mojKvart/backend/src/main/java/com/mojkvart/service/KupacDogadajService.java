package com.mojkvart.service;
import com.mojkvart.domain.Dogadaj;
import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacDogadaj;
import com.mojkvart.model.KupacDogadajDTO;
import com.mojkvart.repos.DogadajRepository;
import com.mojkvart.repos.KupacDogadajRepository;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.util.NotFoundException;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class KupacDogadajService {

    private final KupacDogadajRepository kupacDogadajRepository;
    private final KupacRepository kupacRepository;
    private final DogadajRepository dogadajRepository;

    public KupacDogadajService(
            final KupacDogadajRepository kupacDogadajRepository,
            final KupacRepository kupacRepository, 
            final DogadajRepository dogadajRepository) {
        this.kupacDogadajRepository = kupacDogadajRepository;
        this.kupacRepository = kupacRepository;
        this.dogadajRepository = dogadajRepository;
    }

    public List<KupacDogadaj> findAll() {
        final List<KupacDogadaj> kupacDogadajs = kupacDogadajRepository.findAll(Sort.by("id"));
        return kupacDogadajs.stream()
                .map(kupacDogadaj -> mapToDTO(kupacDogadaj, new KupacDogadaj()))
                .toList();
    }

    public KupacDogadaj get(final Long id) {
        return kupacDogadajRepository.findById(id)
                .map(kupacDogadaj -> mapToDTO(kupacDogadaj, new KupacDogadaj()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final com.mojkvart.model.KupacDogadajDTO kupacDogadajDTO) {
        final KupacDogadaj kupacDogadaj = new KupacDogadaj();
        mapToEntity(kupacDogadajDTO, kupacDogadaj);
        return kupacDogadajRepository.save(kupacDogadaj).getId();
    }
    

    public void update(final Long id, final com.mojkvart.model.KupacDogadajDTO kupacDogadajDTO) {
        final KupacDogadaj kupacDogadaj = kupacDogadajRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(kupacDogadajDTO, kupacDogadaj);
        kupacDogadajRepository.save(kupacDogadaj);
    }

    public void delete(final Long id) {
        kupacDogadajRepository.deleteById(id);
    }

    private KupacDogadaj mapToDTO(final KupacDogadaj kupacDogadaj,
            final KupacDogadaj kupacDogadajDTO) {
        kupacDogadajDTO.setId(kupacDogadaj.getId());
        kupacDogadajDTO.setKupacDogadajFlag(kupacDogadaj.getKupacDogadajFlag());
        kupacDogadajDTO.setKupac(kupacDogadaj.getKupac() == null ? null : kupacDogadaj.getKupac());
        kupacDogadajDTO.setDogadaj(kupacDogadaj.getDogadaj() == null ? null : kupacDogadaj.getDogadaj());
        return kupacDogadajDTO;
    }

    private KupacDogadaj mapToEntity(final KupacDogadajDTO kupacDogadajDTO,
            final KupacDogadaj kupacDogadaj) {
        kupacDogadaj.setKupacDogadajFlag(kupacDogadajDTO.getKupacDogadajFlag());
        final Kupac kupac = kupacDogadajDTO.getKupac() == null ? null : kupacRepository.findById(kupacDogadajDTO.getKupac())
                .orElseThrow(() -> new NotFoundException("kupac not found"));
        kupacDogadaj.setKupac(kupac);
        final Dogadaj dogadaj = kupacDogadajDTO.getDogadaj() == null ? null : dogadajRepository.findById(kupacDogadajDTO.getDogadaj())
                .orElseThrow(() -> new NotFoundException("dogadaj not found"));
        kupacDogadaj.setDogadaj(dogadaj);
        return kupacDogadaj;
    }
}
