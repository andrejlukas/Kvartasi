package com.mojkvart.service;

import com.mojkvart.dtos.PopustDTO;
import com.mojkvart.entities.PonudaPopust;
import com.mojkvart.entities.Popust;
import com.mojkvart.repos.PonudaPopustRepository;
import com.mojkvart.repos.PopustRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PopustService {

    private final PopustRepository popustRepository;
    private final PonudaPopustRepository ponudaPopustRepository;

    public PopustService(final PopustRepository popustRepository,
            final PonudaPopustRepository ponudaPopustRepository) {
        this.popustRepository = popustRepository;
        this.ponudaPopustRepository = ponudaPopustRepository;
    }

    public List<PopustDTO> findAll() {
        final List<Popust> popusts = popustRepository.findAll(Sort.by("popustId"));
        return popusts.stream()
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
        popustDTO.setPopustOpis(popust.getPopustOpis());
        popustDTO.setPonudaPopust(popust.getPonudaPopust() == null ? null : popust.getPonudaPopust().getPonudaPopustId());
        return popustDTO;
    }

    private Popust mapToEntity(final PopustDTO popustDTO, final Popust popust) {
        popust.setPopustQrkod(popustDTO.getPopustQrkod());
        popust.setPopustOpis(popustDTO.getPopustOpis());
        final PonudaPopust ponudaPopust = popustDTO.getPonudaPopust() == null ? null : ponudaPopustRepository.findById(popustDTO.getPonudaPopust())
                .orElseThrow(() -> new NotFoundException("ponudaPopust not found"));
        popust.setPonudaPopust(ponudaPopust);
        return popust;
    }

}
