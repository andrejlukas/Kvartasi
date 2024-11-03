package com.mojkvart.service;

import com.mojkvart.domain.Dogadaj;
import com.mojkvart.domain.KupacDogadajTrgovina;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.DogadajDTO;
import com.mojkvart.repos.DogadajRepository;
import com.mojkvart.repos.KupacDogadajTrgovinaRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DogadajService {

    private final DogadajRepository dogadajRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final KupacDogadajTrgovinaRepository kupacDogadajTrgovinaRepository;

    public DogadajService(final DogadajRepository dogadajRepository,
            final TrgovinaRepository trgovinaRepository,
            final KupacDogadajTrgovinaRepository kupacDogadajTrgovinaRepository) {
        this.dogadajRepository = dogadajRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.kupacDogadajTrgovinaRepository = kupacDogadajTrgovinaRepository;
    }

    public List<DogadajDTO> findAll() {
        final List<Dogadaj> dogadajs = dogadajRepository.findAll(Sort.by("dogadajId"));
        return dogadajs.stream()
                .map(dogadaj -> mapToDTO(dogadaj, new DogadajDTO()))
                .toList();
    }

    public DogadajDTO get(final Integer dogadajId) {
        return dogadajRepository.findById(dogadajId)
                .map(dogadaj -> mapToDTO(dogadaj, new DogadajDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final DogadajDTO dogadajDTO) {
        final Dogadaj dogadaj = new Dogadaj();
        mapToEntity(dogadajDTO, dogadaj);
        return dogadajRepository.save(dogadaj).getDogadajId();
    }

    public void update(final Integer dogadajId, final DogadajDTO dogadajDTO) {
        final Dogadaj dogadaj = dogadajRepository.findById(dogadajId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(dogadajDTO, dogadaj);
        dogadajRepository.save(dogadaj);
    }

    public void delete(final Integer dogadajId) {
        dogadajRepository.deleteById(dogadajId);
    }

    private DogadajDTO mapToDTO(final Dogadaj dogadaj, final DogadajDTO dogadajDTO) {
        dogadajDTO.setDogadajId(dogadaj.getDogadajId());
        dogadajDTO.setDogadajOpis(dogadaj.getDogadajOpis());
        dogadajDTO.setDogadajNaziv(dogadaj.getDogadajNaziv());
        dogadajDTO.setDogadajVrijeme(dogadaj.getDogadajVrijeme());
        dogadajDTO.setDogadajSlika(dogadaj.getDogadajSlika());
        dogadajDTO.setTrgovina(dogadaj.getTrgovina() == null ? null : dogadaj.getTrgovina().getTrgovinaId());
        return dogadajDTO;
    }

    private Dogadaj mapToEntity(final DogadajDTO dogadajDTO, final Dogadaj dogadaj) {
        dogadaj.setDogadajOpis(dogadajDTO.getDogadajOpis());
        dogadaj.setDogadajNaziv(dogadajDTO.getDogadajNaziv());
        dogadaj.setDogadajVrijeme(dogadajDTO.getDogadajVrijeme());
        dogadaj.setDogadajSlika(dogadajDTO.getDogadajSlika());
        final Trgovina trgovina = dogadajDTO.getTrgovina() == null ? null : trgovinaRepository.findById(dogadajDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        dogadaj.setTrgovina(trgovina);
        return dogadaj;
    }

    public ReferencedWarning getReferencedWarning(final Integer dogadajId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Dogadaj dogadaj = dogadajRepository.findById(dogadajId)
                .orElseThrow(NotFoundException::new);
        final KupacDogadajTrgovina dogadajKupacDogadajTrgovina = kupacDogadajTrgovinaRepository.findFirstByDogadaj(dogadaj);
        if (dogadajKupacDogadajTrgovina != null) {
            referencedWarning.setKey("dogadaj.kupacDogadajTrgovina.dogadaj.referenced");
            referencedWarning.addParam(dogadajKupacDogadajTrgovina.getId());
            return referencedWarning;
        }
        return null;
    }

}
