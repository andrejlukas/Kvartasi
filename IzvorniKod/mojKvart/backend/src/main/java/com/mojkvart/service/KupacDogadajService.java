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
import java.util.stream.Collectors;

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

    public List<KupacDogadajDTO> findAll() {
        final List<KupacDogadaj> kupacDogadajs = kupacDogadajRepository.findAll(Sort.by("id"));
        return kupacDogadajs.stream()
                .map(kupacDogadaj -> mapToDTO(kupacDogadaj, new KupacDogadajDTO()))
                .toList();
    }

    // funkcija koja vraca sve kupacDogadaje za odredenog kupca na koje će kupac ići ili je išao
    public List<KupacDogadajDTO> getDogadajiZaKupca(Integer kupacId) {
    List<KupacDogadaj> kupacDogadaji = kupacDogadajRepository.findByKupac_KupacId(kupacId);
    return kupacDogadaji.stream().filter(KupacDogadaj::getKupacDogadajFlag)
            .map(kd -> mapToDTO(kd, new KupacDogadajDTO()))
            .collect(Collectors.toList());
}


    public KupacDogadajDTO get(final Long id) {
        return kupacDogadajRepository.findById(id)
                .map(kupacDogadaj -> mapToDTO(kupacDogadaj, new KupacDogadajDTO()))
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

    private KupacDogadajDTO mapToDTO(final KupacDogadaj kupacDogadaj,
                                 final KupacDogadajDTO kupacDogadajDTO) {
    kupacDogadajDTO.setId(kupacDogadaj.getId());
    kupacDogadajDTO.setKupacDogadajFlag(kupacDogadaj.getKupacDogadajFlag());
    kupacDogadajDTO.setKupac(kupacDogadaj.getKupac() == null ? null : kupacDogadaj.getKupac().getKupacId());
    kupacDogadajDTO.setDogadaj(kupacDogadaj.getDogadaj() == null ? null : kupacDogadaj.getDogadaj().getDogadajId());
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
