package com.mojkvart.service;

import com.mojkvart.dtos.TrgovinaDTO;
import com.mojkvart.entities.Atribut;
import com.mojkvart.entities.KorisnikDogadajTrgovina;
import com.mojkvart.entities.KorisnikTrgovinaPonuda;
import com.mojkvart.entities.KorisnikTrgovinaRecenzija;
import com.mojkvart.entities.Trgovina;
import com.mojkvart.entities.Vlasnik;
import com.mojkvart.repos.AtributRepository;
import com.mojkvart.repos.KorisnikDogadajTrgovinaRepository;
import com.mojkvart.repos.KorisnikTrgovinaPonudaRepository;
import com.mojkvart.repos.KorisnikTrgovinaRecenzijaRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.repos.VlasnikRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class TrgovinaService {

    private final TrgovinaRepository trgovinaRepository;
    private final AtributRepository atributRepository;
    private final VlasnikRepository vlasnikRepository;
    private final KorisnikDogadajTrgovinaRepository korisnikDogadajTrgovinaRepository;
    private final KorisnikTrgovinaRecenzijaRepository korisnikTrgovinaRecenzijaRepository;
    private final KorisnikTrgovinaPonudaRepository korisnikTrgovinaPonudaRepository;

    public TrgovinaService(final TrgovinaRepository trgovinaRepository,
            final AtributRepository atributRepository, final VlasnikRepository vlasnikRepository,
            final KorisnikDogadajTrgovinaRepository korisnikDogadajTrgovinaRepository,
            final KorisnikTrgovinaRecenzijaRepository korisnikTrgovinaRecenzijaRepository,
            final KorisnikTrgovinaPonudaRepository korisnikTrgovinaPonudaRepository) {
        this.trgovinaRepository = trgovinaRepository;
        this.atributRepository = atributRepository;
        this.vlasnikRepository = vlasnikRepository;
        this.korisnikDogadajTrgovinaRepository = korisnikDogadajTrgovinaRepository;
        this.korisnikTrgovinaRecenzijaRepository = korisnikTrgovinaRecenzijaRepository;
        this.korisnikTrgovinaPonudaRepository = korisnikTrgovinaPonudaRepository;
    }

    public List<TrgovinaDTO> findAll() {
        final List<Trgovina> trgovinas = trgovinaRepository.findAll(Sort.by("trgovinaId"));
        return trgovinas.stream()
                .map(trgovina -> mapToDTO(trgovina, new TrgovinaDTO()))
                .toList();
    }

    public TrgovinaDTO get(final Integer trgovinaId) {
        return trgovinaRepository.findById(trgovinaId)
                .map(trgovina -> mapToDTO(trgovina, new TrgovinaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TrgovinaDTO trgovinaDTO) {
        final Trgovina trgovina = new Trgovina();
        mapToEntity(trgovinaDTO, trgovina);
        return trgovinaRepository.save(trgovina).getTrgovinaId();
    }

    public void update(final Integer trgovinaId, final TrgovinaDTO trgovinaDTO) {
        final Trgovina trgovina = trgovinaRepository.findById(trgovinaId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(trgovinaDTO, trgovina);
        trgovinaRepository.save(trgovina);
    }

    public void delete(final Integer trgovinaId) {
        trgovinaRepository.deleteById(trgovinaId);
    }

    private TrgovinaDTO mapToDTO(final Trgovina trgovina, final TrgovinaDTO trgovinaDTO) {
        trgovinaDTO.setTrgovinaId(trgovina.getTrgovinaId());
        trgovinaDTO.setTrgovinaNaziv(trgovina.getTrgovinaNaziv());
        trgovinaDTO.setTrgovinaOpis(trgovina.getTrgovinaOpis());
        trgovinaDTO.setTrgovinaLokacija(trgovina.getTrgovinaLokacija());
        trgovinaDTO.setTrgovinaSlika(trgovina.getTrgovinaSlika());
        trgovinaDTO.setTrgovinaAtributi(trgovina.getTrgovinaAtributi());
        trgovinaDTO.setImaAtributeAtributs(trgovina.getImaAtributeAtributs().stream()
                .map(atribut -> atribut.getAtributId())
                .toList());
        return trgovinaDTO;
    }

    private Trgovina mapToEntity(final TrgovinaDTO trgovinaDTO, final Trgovina trgovina) {
        trgovina.setTrgovinaNaziv(trgovinaDTO.getTrgovinaNaziv());
        trgovina.setTrgovinaOpis(trgovinaDTO.getTrgovinaOpis());
        trgovina.setTrgovinaLokacija(trgovinaDTO.getTrgovinaLokacija());
        trgovina.setTrgovinaSlika(trgovinaDTO.getTrgovinaSlika());
        trgovina.setTrgovinaAtributi(trgovinaDTO.getTrgovinaAtributi());
        final List<Atribut> imaAtributeAtributs = atributRepository.findAllById(
                trgovinaDTO.getImaAtributeAtributs() == null ? Collections.emptyList() : trgovinaDTO.getImaAtributeAtributs());
        if (imaAtributeAtributs.size() != (trgovinaDTO.getImaAtributeAtributs() == null ? 0 : trgovinaDTO.getImaAtributeAtributs().size())) {
            throw new NotFoundException("one of imaAtributeAtributs not found");
        }
        trgovina.setImaAtributeAtributs(new HashSet<>(imaAtributeAtributs));
        return trgovina;
    }

    public ReferencedWarning getReferencedWarning(final Integer trgovinaId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Trgovina trgovina = trgovinaRepository.findById(trgovinaId)
                .orElseThrow(NotFoundException::new);
        final Vlasnik trgovinaVlasnik = vlasnikRepository.findFirstByTrgovina(trgovina);
        if (trgovinaVlasnik != null) {
            referencedWarning.setKey("trgovina.vlasnik.trgovina.referenced");
            referencedWarning.addParam(trgovinaVlasnik.getVlasnikId());
            return referencedWarning;
        }
        final KorisnikDogadajTrgovina trgovinaKorisnikDogadajTrgovina = korisnikDogadajTrgovinaRepository.findFirstByTrgovina(trgovina);
        if (trgovinaKorisnikDogadajTrgovina != null) {
            referencedWarning.setKey("trgovina.korisnikDogadajTrgovina.trgovina.referenced");
            referencedWarning.addParam(trgovinaKorisnikDogadajTrgovina.getId());
            return referencedWarning;
        }
        final KorisnikTrgovinaRecenzija trgovinaKorisnikTrgovinaRecenzija = korisnikTrgovinaRecenzijaRepository.findFirstByTrgovina(trgovina);
        if (trgovinaKorisnikTrgovinaRecenzija != null) {
            referencedWarning.setKey("trgovina.korisnikTrgovinaRecenzija.trgovina.referenced");
            referencedWarning.addParam(trgovinaKorisnikTrgovinaRecenzija.getId());
            return referencedWarning;
        }
        final KorisnikTrgovinaPonuda trgovinaKorisnikTrgovinaPonuda = korisnikTrgovinaPonudaRepository.findFirstByTrgovina(trgovina);
        if (trgovinaKorisnikTrgovinaPonuda != null) {
            referencedWarning.setKey("trgovina.korisnikTrgovinaPonuda.trgovina.referenced");
            referencedWarning.addParam(trgovinaKorisnikTrgovinaPonuda.getId());
            return referencedWarning;
        }
        return null;
    }

}
