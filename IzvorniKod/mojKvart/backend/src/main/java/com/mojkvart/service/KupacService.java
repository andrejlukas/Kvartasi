package com.mojkvart.service;

import com.mojkvart.domain.*;
import com.mojkvart.model.KupacDTO;
import com.mojkvart.repos.*;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class KupacService {

    private final KupacRepository kupacRepository;
    private final KupacDogadajRepository kupacDogadajRepository;
    private final KupacTrgovinaRecenzijaRepository kupacTrgovinaRecenzijaRepository;
    private final OcjenaProizvodKupacRepository ocjenaProizvodKupacRepository;
    private final KupacPonudaPopustRepository kupacTrgovinaPonudaPopustRepository;
    private final KupacProizvodRepository kupacProizvodTrgovinaRepository;

    public KupacService(final KupacRepository kupacRepository,
            final KupacDogadajRepository kupacDogadajRepository,
            final KupacTrgovinaRecenzijaRepository kupacTrgovinaRecenzijaRepository,
            final OcjenaProizvodKupacRepository ocjenaProizvodKupacRepository,
            final KupacPonudaPopustRepository kupacTrgovinaPonudaPopustRepository,
            final KupacProizvodRepository kupacProizvodTrgovinaRepository) {
        this.kupacRepository = kupacRepository;
        this.kupacDogadajRepository = kupacDogadajRepository;
        this.kupacTrgovinaRecenzijaRepository = kupacTrgovinaRecenzijaRepository;
        this.ocjenaProizvodKupacRepository = ocjenaProizvodKupacRepository;
        this.kupacTrgovinaPonudaPopustRepository = kupacTrgovinaPonudaPopustRepository;
        this.kupacProizvodTrgovinaRepository = kupacProizvodTrgovinaRepository;
    }

    public List<KupacDTO> findAll() {
        final List<Kupac> kupacs = kupacRepository.findAll(Sort.by("kupacId"));
        return kupacs.stream()
                .map(kupac -> mapToDTO(kupac, new KupacDTO()))
                .toList();
    }

    public Optional<KupacDTO> findByKupacEmail(String email) {
        return kupacRepository.findByKupacEmail(email)
                .map(kupac -> mapToDTO(kupac, new KupacDTO()));
    }

    public KupacDTO get(final Integer kupacId) {
        return kupacRepository.findById(kupacId)
                .map(kupac -> mapToDTO(kupac, new KupacDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final KupacDTO kupacDTO) {
        final Kupac kupac = new Kupac();
        mapToEntity(kupacDTO, kupac);
        return kupacRepository.save(kupac).getKupacId();
    }

    public void update(final Integer kupacId, final KupacDTO kupacDTO) {
        final Kupac kupac = kupacRepository.findById(kupacId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(kupacDTO, kupac);
        kupacRepository.save(kupac);
    }

    public void delete(final Integer kupacId) {
        kupacRepository.deleteById(kupacId);
    }

    private KupacDTO mapToDTO(final Kupac kupac, final KupacDTO kupacDTO) {
        kupacDTO.setKupacId(kupac.getKupacId());
        kupacDTO.setKupacEmail(kupac.getKupacEmail());
        kupacDTO.setKupacIme(kupac.getKupacIme());
        kupacDTO.setKupacPrezime(kupac.getKupacPrezime());
        kupacDTO.setKupacAdresa(kupac.getKupacAdresa());
        kupacDTO.setKupacSifra(kupac.getKupacSifra());
        return kupacDTO;
    }

    private Kupac mapToEntity(final KupacDTO kupacDTO, final Kupac kupac) {
        kupac.setKupacEmail(kupacDTO.getKupacEmail());
        kupac.setKupacIme(kupacDTO.getKupacIme());
        kupac.setKupacPrezime(kupacDTO.getKupacPrezime());
        kupac.setKupacAdresa(kupacDTO.getKupacAdresa());
        kupac.setKupacSifra(kupacDTO.getKupacSifra());
        return kupac;
    }

    public ReferencedWarning getReferencedWarning(final Integer kupacId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Kupac kupac = kupacRepository.findById(kupacId)
                .orElseThrow(NotFoundException::new);
        final com.mojkvart.domain.KupacDogadaj kupackupacDogadaj = kupacDogadajRepository.findFirstByKupac(kupac);
        if (kupackupacDogadaj != null) {
            referencedWarning.setKey("kupac.kupacDogadaj.kupac.referenced");
            referencedWarning.addParam(kupackupacDogadaj.getId());
            return referencedWarning;
        }
        final KupacTrgovinaRecenzija kupacKupacTrgovinaRecenzija = kupacTrgovinaRecenzijaRepository.findFirstByKupac(kupac);
        if (kupacKupacTrgovinaRecenzija != null) {
            referencedWarning.setKey("kupac.kupacTrgovinaRecenzija.kupac.referenced");
            referencedWarning.addParam(kupacKupacTrgovinaRecenzija.getId());
            return referencedWarning;
        }
        final OcjenaProizvodKupac kupacOcjenaProizvodKupac = ocjenaProizvodKupacRepository.findFirstByKupac(kupac);
        if (kupacOcjenaProizvodKupac != null) {
            referencedWarning.setKey("kupac.ocjenaProizvodKupac.kupac.referenced");
            referencedWarning.addParam(kupacOcjenaProizvodKupac.getId());
            return referencedWarning;
        }
        final KupacPonudaPopust kupacKupacTrgovinaPonudaPopust = kupacTrgovinaPonudaPopustRepository.findFirstByKupac(kupac);
        if (kupacKupacTrgovinaPonudaPopust != null) {
            referencedWarning.setKey("kupac.kupacTrgovinaPonudaPopust.kupac.referenced");
            referencedWarning.addParam(kupacKupacTrgovinaPonudaPopust.getId());
            return referencedWarning;
        }
        final KupacProizvod kupacKupacProizvodTrgovina = kupacProizvodTrgovinaRepository.findFirstByKupac(kupac);
        if (kupacKupacProizvodTrgovina != null) {
            referencedWarning.setKey("kupac.kupacProizvodTrgovina.kupac.referenced");
            referencedWarning.addParam(kupacKupacProizvodTrgovina.getId());
            return referencedWarning;
        }
        return null;
    }

}
