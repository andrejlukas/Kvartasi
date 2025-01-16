package com.mojkvart.service;

import com.mojkvart.domain.KupacPonudaPopust;
import com.mojkvart.domain.Ponuda;
import com.mojkvart.domain.PonudaPopust;
import com.mojkvart.domain.Popust;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.PonudaDTO;
import com.mojkvart.model.PopustDTO;
import com.mojkvart.repos.KupacPonudaPopustRepository;
import com.mojkvart.repos.PonudaPopustRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.repos.PopustRepository;
import com.mojkvart.util.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PopustService {

    private final PopustRepository popustRepository;
    private final PonudaPopustRepository ponudaPopustRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final KupacPonudaPopustRepository kupacPonudaPopustRepository;

    public PopustService(final PopustRepository popustRepository,
            final PonudaPopustRepository ponudaPopustRepository,
             final TrgovinaRepository trgovinaRepository,
              final KupacPonudaPopustRepository kupacPonudaPopustRepository) {
        this.popustRepository = popustRepository;
        this.ponudaPopustRepository = ponudaPopustRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.kupacPonudaPopustRepository = kupacPonudaPopustRepository;
    }

    // prikazi sve popuste koje je moderator odobrio i kupac nije jos spremio
    public List<PopustDTO> findAllWithFlagTrue(Integer kupacId) {
        final List<Popust> popusts = popustRepository.findAll(Sort.by("popustId"));
        final List<KupacPonudaPopust> kupacPonudaPopusts = kupacPonudaPopustRepository.findAll(Sort.by("kupac"));

        // Pronađi sve ponudaPopuste koje je kupac već spremio
        final Set<Integer> spremljeniPopustiIds = kupacPonudaPopusts.stream()
                .filter(kpp -> kpp.getKupac().getKupacId().equals(kupacId))
                .map(kpp -> kpp.getPonudaPopust().getPonudaPopustId())
                .collect(Collectors.toSet());

        // Filtriraj ponude koje imaju flag true i nisu spremljene od strane kupca
        return popusts.stream()
                .filter(popust -> popust.getPonudaPopust() != null &&
                        popust.getPonudaPopust().getPonudaPopustFlag() != null &&
                        popust.getPonudaPopust().getPonudaPopustFlag() &&
                        popust.getPopustRok().isAfter(LocalDateTime.now()) &&
                        !spremljeniPopustiIds.contains(popust.getPonudaPopust().getPonudaPopustId()))
                .map(popust -> mapToDTO(popust, new PopustDTO()))
                .toList();
    }

    // popusti koje moderator treba odobriti, a nisu istekli
    public List<PopustDTO> findAllWithFlagFalse() {
        final List<Popust> popusti = popustRepository.findAll(Sort.by("popustId"));
        return popusti.stream()
                .filter(popust -> popust.getPonudaPopust() != null &&
                        popust.getPonudaPopust().getPonudaPopustFlag() != null &&
                        !popust.getPonudaPopust().getPonudaPopustFlag() &&
                        popust.getPopustRok().isAfter(LocalDateTime.now()))
                .map(popust -> mapToDTO(popust, new PopustDTO()))
                .toList();
    }


    public List<PopustDTO> findAllTrgovinaValidPopusts(Integer trgovinaId) {
        final List<Popust> popusti = popustRepository.findAll(Sort.by("popustId"));
        return popusti.stream()
                .filter(popust -> popust.getPonudaPopust() != null &&
                        popust.getPonudaPopust().getPonudaPopustFlag() != null &&
                        Objects.equals(popust.getPonudaPopust().getTrgovina().getTrgovinaId(), trgovinaId) &&
                        popust.getPonudaPopust().getPonudaPopustFlag() &&
                        popust.getPopustRok().isAfter(LocalDateTime.now()))
                .map(popust -> mapToDTO(popust, new PopustDTO()))
                .toList();
    }

    public List<PopustDTO> findAllTrgovinaNonValidPopusts(Integer trgovinaId) {
        final List<Popust> popusti = popustRepository.findAll(Sort.by("popustId"));
        return popusti.stream()
                .filter(popust -> popust.getPonudaPopust() != null &&
                        popust.getPonudaPopust().getPonudaPopustFlag() != null &&
                        Objects.equals(popust.getPonudaPopust().getTrgovina().getTrgovinaId(), trgovinaId) &&
                        (!popust.getPonudaPopust().getPonudaPopustFlag() ||
                        popust.getPopustRok().isBefore(LocalDateTime.now())))
                .map(popust -> mapToDTO(popust, new PopustDTO()))
                .toList();
    }
    

    public PopustDTO get(final Integer popustId) {
        return popustRepository.findById(popustId)
                .map(popust -> mapToDTO(popust, new PopustDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PopustDTO popustDTO) {
        final Popust popust = new Popust();
        mapToEntity(popustDTO, popust);
        return popustRepository.save(popust).getPopustId();
    }

    public void update(final Integer popustId, final PopustDTO popustDTO) {
        final Popust popust = popustRepository.findById(popustId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(popustDTO, popust);
        popustRepository.save(popust);
    }

    public void delete(final Integer popustId) {
        popustRepository.deleteById(popustId);
    }

    PopustDTO mapToDTO(final Popust popust, final PopustDTO popustDTO) {
        popustDTO.setPopustId(popust.getPopustId());
        popustDTO.setPopustQrkod(popust.getPopustQrkod());
        popustDTO.setPopustNaziv(popust.getPopustNaziv());
        popustDTO.setPopustOpis(popust.getPopustOpis());
        popustDTO.setPopustRok(popust.getPopustRok());
        popustDTO.setPonudaPopust(popust.getPonudaPopust() == null ? null : popust.getPonudaPopust().getPonudaPopustId());
        PonudaPopust ponudaPopust2 = popust.getPonudaPopust();
        Trgovina trgovina2 = trgovinaRepository.findById(ponudaPopust2.getTrgovina().
        getTrgovinaId()).orElseThrow(() -> new RuntimeException("Trgovina nije pronađena"));
        popust.setTrgovinaIme(trgovina2.getTrgovinaNaziv());
        popustDTO.setTrgovinaIme(trgovina2.getTrgovinaNaziv());
        return popustDTO;
    }

    private Popust mapToEntity(final PopustDTO popustDTO, final Popust popust) {
        popust.setPopustQrkod(popustDTO.getPopustQrkod());
        popust.setPopustNaziv(popustDTO.getPopustNaziv());
        popust.setPopustOpis(popustDTO.getPopustOpis());
        popust.setPopustRok(popustDTO.getPopustRok());
        final PonudaPopust ponudaPopust = popustDTO.getPonudaPopust() == null ? null : ponudaPopustRepository.findById(popustDTO.getPonudaPopust())
                .orElseThrow(() -> new NotFoundException("ponudaPopust not found"));
        popust.setPonudaPopust(ponudaPopust);
        return popust;
    }

}
