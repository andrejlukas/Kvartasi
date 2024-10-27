package com.mojkvart.service;

import com.mojkvart.dtos.KorisnikTrgovinaRecenzijaDTO;
import com.mojkvart.entities.Korisnik;
import com.mojkvart.entities.KorisnikTrgovinaRecenzija;
import com.mojkvart.entities.Recenzija;
import com.mojkvart.entities.Trgovina;
import com.mojkvart.repos.KorisnikRepository;
import com.mojkvart.repos.KorisnikTrgovinaRecenzijaRepository;
import com.mojkvart.repos.RecenzijaRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class KorisnikTrgovinaRecenzijaService {

    private final KorisnikTrgovinaRecenzijaRepository korisnikTrgovinaRecenzijaRepository;
    private final KorisnikRepository korisnikRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final RecenzijaRepository recenzijaRepository;

    public KorisnikTrgovinaRecenzijaService(
            final KorisnikTrgovinaRecenzijaRepository korisnikTrgovinaRecenzijaRepository,
            final KorisnikRepository korisnikRepository,
            final TrgovinaRepository trgovinaRepository,
            final RecenzijaRepository recenzijaRepository) {
        this.korisnikTrgovinaRecenzijaRepository = korisnikTrgovinaRecenzijaRepository;
        this.korisnikRepository = korisnikRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.recenzijaRepository = recenzijaRepository;
    }

    public List<KorisnikTrgovinaRecenzijaDTO> findAll() {
        final List<KorisnikTrgovinaRecenzija> korisnikTrgovinaRecenzijas = korisnikTrgovinaRecenzijaRepository.findAll(Sort.by("id"));
        return korisnikTrgovinaRecenzijas.stream()
                .map(korisnikTrgovinaRecenzija -> mapToDTO(korisnikTrgovinaRecenzija, new KorisnikTrgovinaRecenzijaDTO()))
                .toList();
    }

    public KorisnikTrgovinaRecenzijaDTO get(final Long id) {
        return korisnikTrgovinaRecenzijaRepository.findById(id)
                .map(korisnikTrgovinaRecenzija -> mapToDTO(korisnikTrgovinaRecenzija, new KorisnikTrgovinaRecenzijaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final KorisnikTrgovinaRecenzijaDTO korisnikTrgovinaRecenzijaDTO) {
        final KorisnikTrgovinaRecenzija korisnikTrgovinaRecenzija = new KorisnikTrgovinaRecenzija();
        mapToEntity(korisnikTrgovinaRecenzijaDTO, korisnikTrgovinaRecenzija);
        return korisnikTrgovinaRecenzijaRepository.save(korisnikTrgovinaRecenzija).getId();
    }

    public void update(final Long id,
            final KorisnikTrgovinaRecenzijaDTO korisnikTrgovinaRecenzijaDTO) {
        final KorisnikTrgovinaRecenzija korisnikTrgovinaRecenzija = korisnikTrgovinaRecenzijaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(korisnikTrgovinaRecenzijaDTO, korisnikTrgovinaRecenzija);
        korisnikTrgovinaRecenzijaRepository.save(korisnikTrgovinaRecenzija);
    }

    public void delete(final Long id) {
        korisnikTrgovinaRecenzijaRepository.deleteById(id);
    }

    private KorisnikTrgovinaRecenzijaDTO mapToDTO(
            final KorisnikTrgovinaRecenzija korisnikTrgovinaRecenzija,
            final KorisnikTrgovinaRecenzijaDTO korisnikTrgovinaRecenzijaDTO) {
        korisnikTrgovinaRecenzijaDTO.setId(korisnikTrgovinaRecenzija.getId());
        korisnikTrgovinaRecenzijaDTO.setKorisnik(korisnikTrgovinaRecenzija.getKorisnik() == null ? null : korisnikTrgovinaRecenzija.getKorisnik().getKorisnikId());
        korisnikTrgovinaRecenzijaDTO.setTrgovina(korisnikTrgovinaRecenzija.getTrgovina() == null ? null : korisnikTrgovinaRecenzija.getTrgovina().getTrgovinaId());
        korisnikTrgovinaRecenzijaDTO.setRecenzija(korisnikTrgovinaRecenzija.getRecenzija() == null ? null : korisnikTrgovinaRecenzija.getRecenzija().getRecenzijaId());
        return korisnikTrgovinaRecenzijaDTO;
    }

    private KorisnikTrgovinaRecenzija mapToEntity(
            final KorisnikTrgovinaRecenzijaDTO korisnikTrgovinaRecenzijaDTO,
            final KorisnikTrgovinaRecenzija korisnikTrgovinaRecenzija) {
        final Korisnik korisnik = korisnikTrgovinaRecenzijaDTO.getKorisnik() == null ? null : korisnikRepository.findById(korisnikTrgovinaRecenzijaDTO.getKorisnik())
                .orElseThrow(() -> new NotFoundException("korisnik not found"));
        korisnikTrgovinaRecenzija.setKorisnik(korisnik);
        final Trgovina trgovina = korisnikTrgovinaRecenzijaDTO.getTrgovina() == null ? null : trgovinaRepository.findById(korisnikTrgovinaRecenzijaDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        korisnikTrgovinaRecenzija.setTrgovina(trgovina);
        final Recenzija recenzija = korisnikTrgovinaRecenzijaDTO.getRecenzija() == null ? null : recenzijaRepository.findById(korisnikTrgovinaRecenzijaDTO.getRecenzija())
                .orElseThrow(() -> new NotFoundException("recenzija not found"));
        korisnikTrgovinaRecenzija.setRecenzija(recenzija);
        return korisnikTrgovinaRecenzija;
    }

}
