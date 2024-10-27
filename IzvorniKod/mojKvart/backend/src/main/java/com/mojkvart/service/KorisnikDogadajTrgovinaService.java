package com.mojkvart.service;

import com.mojkvart.dtos.KorisnikDogadajTrgovinaDTO;
import com.mojkvart.entities.Dogadaj;
import com.mojkvart.entities.Korisnik;
import com.mojkvart.entities.KorisnikDogadajTrgovina;
import com.mojkvart.entities.Trgovina;
import com.mojkvart.repos.DogadajRepository;
import com.mojkvart.repos.KorisnikDogadajTrgovinaRepository;
import com.mojkvart.repos.KorisnikRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class KorisnikDogadajTrgovinaService {

    private final KorisnikDogadajTrgovinaRepository korisnikDogadajTrgovinaRepository;
    private final KorisnikRepository korisnikRepository;
    private final DogadajRepository dogadajRepository;
    private final TrgovinaRepository trgovinaRepository;

    public KorisnikDogadajTrgovinaService(
            final KorisnikDogadajTrgovinaRepository korisnikDogadajTrgovinaRepository,
            final KorisnikRepository korisnikRepository, final DogadajRepository dogadajRepository,
            final TrgovinaRepository trgovinaRepository) {
        this.korisnikDogadajTrgovinaRepository = korisnikDogadajTrgovinaRepository;
        this.korisnikRepository = korisnikRepository;
        this.dogadajRepository = dogadajRepository;
        this.trgovinaRepository = trgovinaRepository;
    }

    public List<KorisnikDogadajTrgovinaDTO> findAll() {
        final List<KorisnikDogadajTrgovina> korisnikDogadajTrgovinas = korisnikDogadajTrgovinaRepository.findAll(Sort.by("id"));
        return korisnikDogadajTrgovinas.stream()
                .map(korisnikDogadajTrgovina -> mapToDTO(korisnikDogadajTrgovina, new KorisnikDogadajTrgovinaDTO()))
                .toList();
    }

    public KorisnikDogadajTrgovinaDTO get(final Long id) {
        return korisnikDogadajTrgovinaRepository.findById(id)
                .map(korisnikDogadajTrgovina -> mapToDTO(korisnikDogadajTrgovina, new KorisnikDogadajTrgovinaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final KorisnikDogadajTrgovinaDTO korisnikDogadajTrgovinaDTO) {
        final KorisnikDogadajTrgovina korisnikDogadajTrgovina = new KorisnikDogadajTrgovina();
        mapToEntity(korisnikDogadajTrgovinaDTO, korisnikDogadajTrgovina);
        return korisnikDogadajTrgovinaRepository.save(korisnikDogadajTrgovina).getId();
    }

    public void update(final Long id, final KorisnikDogadajTrgovinaDTO korisnikDogadajTrgovinaDTO) {
        final KorisnikDogadajTrgovina korisnikDogadajTrgovina = korisnikDogadajTrgovinaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(korisnikDogadajTrgovinaDTO, korisnikDogadajTrgovina);
        korisnikDogadajTrgovinaRepository.save(korisnikDogadajTrgovina);
    }

    public void delete(final Long id) {
        korisnikDogadajTrgovinaRepository.deleteById(id);
    }

    private KorisnikDogadajTrgovinaDTO mapToDTO(
            final KorisnikDogadajTrgovina korisnikDogadajTrgovina,
            final KorisnikDogadajTrgovinaDTO korisnikDogadajTrgovinaDTO) {
        korisnikDogadajTrgovinaDTO.setId(korisnikDogadajTrgovina.getId());
        korisnikDogadajTrgovinaDTO.setKorisnik(korisnikDogadajTrgovina.getKorisnik() == null ? null : korisnikDogadajTrgovina.getKorisnik().getKorisnikId());
        korisnikDogadajTrgovinaDTO.setDogadaj(korisnikDogadajTrgovina.getDogadaj() == null ? null : korisnikDogadajTrgovina.getDogadaj().getDogadajId());
        korisnikDogadajTrgovinaDTO.setTrgovina(korisnikDogadajTrgovina.getTrgovina() == null ? null : korisnikDogadajTrgovina.getTrgovina().getTrgovinaId());
        return korisnikDogadajTrgovinaDTO;
    }

    private KorisnikDogadajTrgovina mapToEntity(
            final KorisnikDogadajTrgovinaDTO korisnikDogadajTrgovinaDTO,
            final KorisnikDogadajTrgovina korisnikDogadajTrgovina) {
        final Korisnik korisnik = korisnikDogadajTrgovinaDTO.getKorisnik() == null ? null : korisnikRepository.findById(korisnikDogadajTrgovinaDTO.getKorisnik())
                .orElseThrow(() -> new NotFoundException("korisnik not found"));
        korisnikDogadajTrgovina.setKorisnik(korisnik);
        final Dogadaj dogadaj = korisnikDogadajTrgovinaDTO.getDogadaj() == null ? null : dogadajRepository.findById(korisnikDogadajTrgovinaDTO.getDogadaj())
                .orElseThrow(() -> new NotFoundException("dogadaj not found"));
        korisnikDogadajTrgovina.setDogadaj(dogadaj);
        final Trgovina trgovina = korisnikDogadajTrgovinaDTO.getTrgovina() == null ? null : trgovinaRepository.findById(korisnikDogadajTrgovinaDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        korisnikDogadajTrgovina.setTrgovina(trgovina);
        return korisnikDogadajTrgovina;
    }

}
