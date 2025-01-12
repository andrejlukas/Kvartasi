package com.mojkvart.service;

import com.mojkvart.domain.Dogadaj;
import com.mojkvart.domain.KupacDogadaj;
import com.mojkvart.domain.Recenzija;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.DogadajDTO;
import com.mojkvart.model.RecenzijaDTO;
import com.mojkvart.repos.DogadajRepository;
import com.mojkvart.repos.KupacDogadajRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DogadajService {
    private final DogadajRepository dogadajRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final KupacDogadajRepository kupacDogadajRepository;

    public DogadajService(final DogadajRepository dogadajRepository,
            final TrgovinaRepository trgovinaRepository,
            final KupacDogadajRepository kupacDogadajRepository) {
        this.dogadajRepository = dogadajRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.kupacDogadajRepository = kupacDogadajRepository;
    }

    public static LocalDateTime getVrijeme(String formmatedString) {
        String date = formmatedString.split(" ")[0];
        if(date.length() != 11) throw new RuntimeException("Datum i vrijeme događaja mora biti u formatu \"dd.MM.gggg. ss:mm\"!");
        String time = formmatedString.split(" ")[1];
        if(time.length() != 5) throw new RuntimeException("Datum i vrijeme događaja mora biti u formatu \"dd.MM.gggg. ss:mm\"!");
        int day = Integer.parseInt(date.split("\\.")[0]);
        int month = Integer.parseInt(date.split("\\.")[1]);
        int year = Integer.parseInt(date.split("\\.")[2]);
        int hour = Integer.parseInt(time.split(":")[0]);
        int minutes = Integer.parseInt(time.split(":")[1]);
        return LocalDateTime.of(year, month, day, hour, minutes);
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

    public List<DogadajDTO> getUpcomingTrgovinasDogadaj(Integer trgovinaId) {
        // Provera postojanja trgovine
        if (!trgovinaRepository.existsById(trgovinaId)) {
            throw new NotFoundException("Trgovina sa ID " + trgovinaId + " nije pronađena");
        }
        List<Dogadaj> listaDogadaja = dogadajRepository.findByTrgovina_TrgovinaId(trgovinaId);
        return listaDogadaja.stream()
                .map(dogadaj -> mapToDTO(dogadaj, new DogadajDTO()))
                .filter(d -> getVrijeme(d.getDogadajVrijeme()).isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());  
    }

    public List<DogadajDTO> getFinishedTrgovinasDogadaj(Integer trgovinaId) {
        // Provera postojanja trgovine
        if (!trgovinaRepository.existsById(trgovinaId)) {
            throw new NotFoundException("Trgovina sa ID " + trgovinaId + " nije pronađena");
        }
        List<Dogadaj> listaDogadaja = dogadajRepository.findByTrgovina_TrgovinaId(trgovinaId);
        return listaDogadaja.stream()
                .map(dogadaj -> mapToDTO(dogadaj, new DogadajDTO()))
                .filter(d -> getVrijeme(d.getDogadajVrijeme()).isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
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
        final KupacDogadaj dogadajKupacDogadaj = kupacDogadajRepository.findFirstByDogadaj(dogadaj);
        if (dogadajKupacDogadaj != null) {
            referencedWarning.setKey("dogadaj.kupacDogadaj.dogadaj.referenced");
            referencedWarning.addParam(dogadajKupacDogadaj.getId());
            return referencedWarning;
        }
        return null;
    }

}
