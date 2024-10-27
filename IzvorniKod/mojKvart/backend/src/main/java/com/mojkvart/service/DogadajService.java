package com.mojkvart.service;

import com.mojkvart.dtos.DogadajDTO;
import com.mojkvart.entities.Dogadaj;
import com.mojkvart.entities.KorisnikDogadajTrgovina;
import com.mojkvart.repos.DogadajRepository;
import com.mojkvart.repos.KorisnikDogadajTrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DogadajService {

    private final DogadajRepository dogadajRepository;
    private final KorisnikDogadajTrgovinaRepository korisnikDogadajTrgovinaRepository;

    public DogadajService(final DogadajRepository dogadajRepository,
            final KorisnikDogadajTrgovinaRepository korisnikDogadajTrgovinaRepository) {
        this.dogadajRepository = dogadajRepository;
        this.korisnikDogadajTrgovinaRepository = korisnikDogadajTrgovinaRepository;
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
        return dogadajDTO;
    }

    private Dogadaj mapToEntity(final DogadajDTO dogadajDTO, final Dogadaj dogadaj) {
        dogadaj.setDogadajOpis(dogadajDTO.getDogadajOpis());
        dogadaj.setDogadajNaziv(dogadajDTO.getDogadajNaziv());
        dogadaj.setDogadajVrijeme(dogadajDTO.getDogadajVrijeme());
        dogadaj.setDogadajSlika(dogadajDTO.getDogadajSlika());
        return dogadaj;
    }

    public ReferencedWarning getReferencedWarning(final Integer dogadajId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Dogadaj dogadaj = dogadajRepository.findById(dogadajId)
                .orElseThrow(NotFoundException::new);
        final KorisnikDogadajTrgovina dogadajKorisnikDogadajTrgovina = korisnikDogadajTrgovinaRepository.findFirstByDogadaj(dogadaj);
        if (dogadajKorisnikDogadajTrgovina != null) {
            referencedWarning.setKey("dogadaj.korisnikDogadajTrgovina.dogadaj.referenced");
            referencedWarning.addParam(dogadajKorisnikDogadajTrgovina.getId());
            return referencedWarning;
        }
        return null;
    }

}
