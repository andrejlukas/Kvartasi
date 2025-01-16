package com.mojkvart.service;

import com.mojkvart.domain.Atribut;
import com.mojkvart.domain.Dogadaj;
import com.mojkvart.domain.PonudaPopust;
import com.mojkvart.domain.Proizvod;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.AtributDTO;
import com.mojkvart.model.TrgovinaDTO;
import com.mojkvart.repos.AtributRepository;
import com.mojkvart.repos.DogadajRepository;
import com.mojkvart.repos.KupacProizvodRepository;
import com.mojkvart.repos.KupacPonudaPopustRepository;
import com.mojkvart.repos.PonudaPopustRepository;
import com.mojkvart.repos.ProizvodRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class TrgovinaService {

    private final TrgovinaRepository trgovinaRepository;
    private final AtributRepository atributRepository;
    private final ProizvodRepository proizvodRepository;
    private final DogadajRepository dogadajRepository;
    private final PonudaPopustRepository ponudaPopustRepository;

    public TrgovinaService(final TrgovinaRepository trgovinaRepository,
            final AtributRepository atributRepository, final ProizvodRepository proizvodRepository,
            final DogadajRepository dogadajRepository,
            final PonudaPopustRepository ponudaPopustRepository,
            final KupacPonudaPopustRepository kupacTrgovinaPonudaPopustRepository,
            final KupacProizvodRepository kupacProizvodTrgovinaRepository) {
        this.trgovinaRepository = trgovinaRepository;
        this.atributRepository = atributRepository;
        this.proizvodRepository = proizvodRepository;
        this.dogadajRepository = dogadajRepository;
        this.ponudaPopustRepository = ponudaPopustRepository;
    }

    public static Boolean containsAllOfTheseWords(String[] wordsToCheck, String checker) {
        for(String word : Stream.of(wordsToCheck).filter(s -> !s.isEmpty()).toList())
            if (!checker.contains(word)) return false;
        return true;
    }

    public List<TrgovinaDTO> findAll() {
        final List<Trgovina> trgovinas = trgovinaRepository.findAll(Sort.by("trgovinaId"));
        return trgovinas.stream()
                .map(trgovina -> mapToDTO(trgovina, new TrgovinaDTO()))
                .toList();
    }

    public Optional<TrgovinaDTO> findByTrgovinaEmail(String email) {
        return trgovinaRepository.findByTrgovinaEmail(email)
                .map(trgovina -> mapToDTO(trgovina, new TrgovinaDTO()));
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

    public List<TrgovinaDTO> getAllTrgovinasBySearch(String input) {
        String[] keyWords = input.toLowerCase().split("\\s+");
        return trgovinaRepository.findAll().stream().filter(
    t -> containsAllOfTheseWords(keyWords, t.getTrgovinaNaziv().toLowerCase()) ||
            containsAllOfTheseWords(keyWords, t.getTrgovinaKategorija().toLowerCase()) ||
            containsAllOfTheseWords(keyWords, t.getTrgovinaOpis().toLowerCase())
        ).map(t -> mapToDTO(t, new TrgovinaDTO())).toList();
    }

    private TrgovinaDTO mapToDTO(final Trgovina trgovina, final TrgovinaDTO trgovinaDTO) {
        trgovinaDTO.setTrgovinaId(trgovina.getTrgovinaId());
        trgovinaDTO.setTrgovinaEmail(trgovina.getTrgovinaEmail());
        trgovinaDTO.setTrgovinaNaziv(trgovina.getTrgovinaNaziv());
        trgovinaDTO.setTrgovinaOpis(trgovina.getTrgovinaOpis());
        trgovinaDTO.setTrgovinaKategorija(trgovina.getTrgovinaKategorija());
        trgovinaDTO.setTrgovinaLokacija(trgovina.getTrgovinaLokacija());
        trgovinaDTO.setTrgovinaSlika(trgovina.getTrgovinaSlika());
        trgovinaDTO.setTrgovinaRadnoVrijemeOd(trgovina.getTrgovinaRadnoVrijemeOd());
        trgovinaDTO.setTrgovinaRadnoVrijemeDo(trgovina.getTrgovinaRadnoVrijemeDo());
        trgovinaDTO.setTrgovinaSifra(trgovina.getTrgovinaSifra());
        trgovinaDTO.setImaAtributeAtributs(trgovina.getImaAtributeAtributs().stream()
                .map(Atribut::getAtributId)
                .collect(Collectors.toSet()));
        return trgovinaDTO;
    }

    private Trgovina mapToEntity(final TrgovinaDTO trgovinaDTO, final Trgovina trgovina) {
        trgovina.setTrgovinaEmail(trgovinaDTO.getTrgovinaEmail());
        trgovina.setTrgovinaNaziv(trgovinaDTO.getTrgovinaNaziv());
        trgovina.setTrgovinaOpis(trgovinaDTO.getTrgovinaOpis());
        trgovina.setTrgovinaKategorija(trgovinaDTO.getTrgovinaKategorija());
        trgovina.setTrgovinaLokacija(trgovinaDTO.getTrgovinaLokacija());
        trgovina.setTrgovinaSlika(trgovinaDTO.getTrgovinaSlika());
        trgovina.setTrgovinaRadnoVrijemeOd(trgovinaDTO.getTrgovinaRadnoVrijemeOd());
        trgovina.setTrgovinaRadnoVrijemeDo(trgovinaDTO.getTrgovinaRadnoVrijemeDo());
        trgovina.setTrgovinaSifra(trgovinaDTO.getTrgovinaSifra());
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
        final Proizvod trgovinaProizvod = proizvodRepository.findFirstByTrgovina(trgovina);
        if (trgovinaProizvod != null) {
            referencedWarning.setKey("trgovina.proizvod.trgovina.referenced");
            referencedWarning.addParam(trgovinaProizvod.getProizvodId());
            return referencedWarning;
        }
        final Dogadaj trgovinaDogadaj = dogadajRepository.findFirstByTrgovina(trgovina);
        if (trgovinaDogadaj != null) {
            referencedWarning.setKey("trgovina.dogadaj.trgovina.referenced");
            referencedWarning.addParam(trgovinaDogadaj.getDogadajId());
            return referencedWarning;
        }
        final PonudaPopust trgovinaPonudaPopust = ponudaPopustRepository.findFirstByTrgovina(trgovina);
        if (trgovinaPonudaPopust != null) {
            referencedWarning.setKey("trgovina.ponudaPopust.trgovina.referenced");
            referencedWarning.addParam(trgovinaPonudaPopust.getPonudaPopustId());
            return referencedWarning;
        }
        return null;
    }
}
