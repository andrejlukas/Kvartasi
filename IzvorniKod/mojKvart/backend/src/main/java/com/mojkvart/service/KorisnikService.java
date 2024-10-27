package com.mojkvart.service;

import com.mojkvart.dtos.KorisnikDTO;
import com.mojkvart.entities.Korisnik;
import com.mojkvart.entities.KorisnikDogadajTrgovina;
import com.mojkvart.entities.KorisnikTrgovinaPonuda;
import com.mojkvart.entities.KorisnikTrgovinaRecenzija;
import com.mojkvart.entities.OcjenaProizvodKorisnik;
import com.mojkvart.repos.KorisnikDogadajTrgovinaRepository;
import com.mojkvart.repos.KorisnikRepository;
import com.mojkvart.repos.KorisnikTrgovinaPonudaRepository;
import com.mojkvart.repos.KorisnikTrgovinaRecenzijaRepository;
import com.mojkvart.repos.OcjenaProizvodKorisnikRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final KorisnikDogadajTrgovinaRepository korisnikDogadajTrgovinaRepository;
    private final KorisnikTrgovinaRecenzijaRepository korisnikTrgovinaRecenzijaRepository;
    private final OcjenaProizvodKorisnikRepository ocjenaProizvodKorisnikRepository;
    private final KorisnikTrgovinaPonudaRepository korisnikTrgovinaPonudaRepository;

    public KorisnikService(final KorisnikRepository korisnikRepository,
            final KorisnikDogadajTrgovinaRepository korisnikDogadajTrgovinaRepository,
            final KorisnikTrgovinaRecenzijaRepository korisnikTrgovinaRecenzijaRepository,
            final OcjenaProizvodKorisnikRepository ocjenaProizvodKorisnikRepository,
            final KorisnikTrgovinaPonudaRepository korisnikTrgovinaPonudaRepository) {
        this.korisnikRepository = korisnikRepository;
        this.korisnikDogadajTrgovinaRepository = korisnikDogadajTrgovinaRepository;
        this.korisnikTrgovinaRecenzijaRepository = korisnikTrgovinaRecenzijaRepository;
        this.ocjenaProizvodKorisnikRepository = ocjenaProizvodKorisnikRepository;
        this.korisnikTrgovinaPonudaRepository = korisnikTrgovinaPonudaRepository;
    }

    public List<KorisnikDTO> findAll() {
        final List<Korisnik> korisniks = korisnikRepository.findAll(Sort.by("korisnikId"));
        return korisniks.stream()
                .map(korisnik -> mapToDTO(korisnik, new KorisnikDTO()))
                .toList();
    }

    public KorisnikDTO get(final Integer korisnikId) {
        return korisnikRepository.findById(korisnikId)
                .map(korisnik -> mapToDTO(korisnik, new KorisnikDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final KorisnikDTO korisnikDTO) {
        final Korisnik korisnik = new Korisnik();
        mapToEntity(korisnikDTO, korisnik);
        return korisnikRepository.save(korisnik).getKorisnikId();
    }

    public void update(final Integer korisnikId, final KorisnikDTO korisnikDTO) {
        final Korisnik korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(korisnikDTO, korisnik);
        korisnikRepository.save(korisnik);
    }

    public void delete(final Integer korisnikId) {
        korisnikRepository.deleteById(korisnikId);
    }

    private KorisnikDTO mapToDTO(final Korisnik korisnik, final KorisnikDTO korisnikDTO) {
        korisnikDTO.setKorisnikId(korisnik.getKorisnikId());
        korisnikDTO.setKorisnikEmail(korisnik.getKorisnikEmail());
        korisnikDTO.setKorisnikIme(korisnik.getKorisnikIme());
        korisnikDTO.setKorisnikPrezime(korisnik.getKorisnikPrezime());
        korisnikDTO.setKorisnikAdresa(korisnik.getKorisnikAdresa());
        korisnikDTO.setKorisnikSifra(korisnik.getKorisnikSifra());
        return korisnikDTO;
    }

    private Korisnik mapToEntity(final KorisnikDTO korisnikDTO, final Korisnik korisnik) {
        korisnik.setKorisnikEmail(korisnikDTO.getKorisnikEmail());
        korisnik.setKorisnikIme(korisnikDTO.getKorisnikIme());
        korisnik.setKorisnikPrezime(korisnikDTO.getKorisnikPrezime());
        korisnik.setKorisnikAdresa(korisnikDTO.getKorisnikAdresa());
        korisnik.setKorisnikSifra(korisnikDTO.getKorisnikSifra());
        return korisnik;
    }

    public ReferencedWarning getReferencedWarning(final Integer korisnikId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Korisnik korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(NotFoundException::new);
        final KorisnikDogadajTrgovina korisnikKorisnikDogadajTrgovina = korisnikDogadajTrgovinaRepository.findFirstByKorisnik(korisnik);
        if (korisnikKorisnikDogadajTrgovina != null) {
            referencedWarning.setKey("korisnik.korisnikDogadajTrgovina.korisnik.referenced");
            referencedWarning.addParam(korisnikKorisnikDogadajTrgovina.getId());
            return referencedWarning;
        }
        final KorisnikTrgovinaRecenzija korisnikKorisnikTrgovinaRecenzija = korisnikTrgovinaRecenzijaRepository.findFirstByKorisnik(korisnik);
        if (korisnikKorisnikTrgovinaRecenzija != null) {
            referencedWarning.setKey("korisnik.korisnikTrgovinaRecenzija.korisnik.referenced");
            referencedWarning.addParam(korisnikKorisnikTrgovinaRecenzija.getId());
            return referencedWarning;
        }
        final OcjenaProizvodKorisnik korisnikOcjenaProizvodKorisnik = ocjenaProizvodKorisnikRepository.findFirstByKorisnik(korisnik);
        if (korisnikOcjenaProizvodKorisnik != null) {
            referencedWarning.setKey("korisnik.ocjenaProizvodKorisnik.korisnik.referenced");
            referencedWarning.addParam(korisnikOcjenaProizvodKorisnik.getId());
            return referencedWarning;
        }
        final KorisnikTrgovinaPonuda korisnikKorisnikTrgovinaPonuda = korisnikTrgovinaPonudaRepository.findFirstByKorisnik(korisnik);
        if (korisnikKorisnikTrgovinaPonuda != null) {
            referencedWarning.setKey("korisnik.korisnikTrgovinaPonuda.korisnik.referenced");
            referencedWarning.addParam(korisnikKorisnikTrgovinaPonuda.getId());
            return referencedWarning;
        }
        return null;
    }

}
