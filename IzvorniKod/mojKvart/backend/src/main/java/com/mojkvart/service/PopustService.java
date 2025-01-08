package com.mojkvart.service;

import com.mojkvart.domain.PonudaPopust;
import com.mojkvart.domain.Popust;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.PopustDTO;
import com.mojkvart.repos.PonudaPopustRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.repos.PopustRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PopustService {

    private final PopustRepository popustRepository;
    private final PonudaPopustRepository ponudaPopustRepository;
    private final TrgovinaRepository trgovinaRepository;
    public PopustService(final PopustRepository popustRepository,
            final PonudaPopustRepository ponudaPopustRepository,
             final TrgovinaRepository trgovinaRepository) {
        this.popustRepository = popustRepository;
        this.ponudaPopustRepository = ponudaPopustRepository;
        this.trgovinaRepository = trgovinaRepository;
    }

    public List<PopustDTO> findAllWithFlagTrue() {
        final List<Popust> popusti = popustRepository.findAll(Sort.by("popustId"));
        return popusti.stream()
                .filter(popust -> popust.getPonudaPopust() != null &&
                        popust.getPonudaPopust().getPonudaPopustFlag() != null &&
                        popust.getPonudaPopust().getPonudaPopustFlag())
                .map(popust -> mapToDTO(popust, new PopustDTO()))
                .toList();
    }

    public List<PopustDTO> findAllWithFlagFalse() {
        final List<Popust> popusti = popustRepository.findAll(Sort.by("popustId"));
        return popusti.stream()
                .filter(popust -> popust.getPonudaPopust() != null &&
                        popust.getPonudaPopust().getPonudaPopustFlag() != null &&
                        !popust.getPonudaPopust().getPonudaPopustFlag())
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

    private PopustDTO mapToDTO(final Popust popust, final PopustDTO popustDTO) {
        popustDTO.setPopustId(popust.getPopustId());
        popustDTO.setPopustQrkod(popust.getPopustQrkod());
        popustDTO.setPopustNaziv(popust.getPopustNaziv());
        popustDTO.setPopustOpis(popust.getPopustOpis());
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
        final PonudaPopust ponudaPopust = popustDTO.getPonudaPopust() == null ? null : ponudaPopustRepository.findById(popustDTO.getPonudaPopust())
                .orElseThrow(() -> new NotFoundException("ponudaPopust not found"));
        popust.setPonudaPopust(ponudaPopust);
        return popust;
    }

}
