package com.mojkvart.service;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacPonudaPopust;
import com.mojkvart.domain.PonudaPopust;
import com.mojkvart.model.KupacPonudaPopustDTO;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.repos.KupacPonudaPopustRepository;
import com.mojkvart.repos.PonudaPopustRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class KupacPonudaPopustService {

    private final KupacPonudaPopustRepository kupacPonudaPopustRepository;
    private final KupacRepository kupacRepository;
    private final PonudaPopustRepository ponudaPopustRepository;

    public KupacPonudaPopustService(
            final KupacPonudaPopustRepository kupacPonudaPopustRepository,
            final KupacRepository kupacRepository, final TrgovinaRepository trgovinaRepository,
            final PonudaPopustRepository ponudaPopustRepository) {
        this.kupacPonudaPopustRepository = kupacPonudaPopustRepository;
        this.kupacRepository = kupacRepository;
        this.ponudaPopustRepository = ponudaPopustRepository;
    }

    public List<KupacPonudaPopustDTO> findAll() {
        final List<KupacPonudaPopust> kupacPonudaPopusts = kupacPonudaPopustRepository.findAll(Sort.by("id"));
        return kupacPonudaPopusts.stream()
                .map(kupacPonudaPopust -> mapToDTO(kupacPonudaPopust, new KupacPonudaPopustDTO()))
                .toList();
    }

    public KupacPonudaPopustDTO get(final Long id) {
        return kupacPonudaPopustRepository.findById(id)
                .map(kupacPonudaPopust -> mapToDTO(kupacPonudaPopust, new KupacPonudaPopustDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final KupacPonudaPopustDTO kupacPonudaPopustDTO) {
        final KupacPonudaPopust kupacPonudaPopust = new KupacPonudaPopust();
        mapToEntity(kupacPonudaPopustDTO, kupacPonudaPopust);
        return kupacPonudaPopustRepository.save(kupacPonudaPopust).getId();
    }

    public void update(final Long id,
            final KupacPonudaPopustDTO kupacPonudaPopustDTO) {
        final KupacPonudaPopust kupacPonudaPopust = kupacPonudaPopustRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(kupacPonudaPopustDTO, kupacPonudaPopust);
        kupacPonudaPopustRepository.save(kupacPonudaPopust);
    }

    public void delete(final Long id) {
        kupacPonudaPopustRepository.deleteById(id);
    }

    private KupacPonudaPopustDTO mapToDTO(
            final KupacPonudaPopust kupacPonudaPopust,
            final KupacPonudaPopustDTO kupacPonudaPopustDTO) {
        kupacPonudaPopustDTO.setId(kupacPonudaPopust.getId());
        kupacPonudaPopustDTO.setKupacPonudaPopustFlag(kupacPonudaPopust.getKupacPonudaPopustFlag());
        kupacPonudaPopustDTO.setKupac(kupacPonudaPopust.getKupac() == null ? null : kupacPonudaPopust.getKupac().getKupacId());
        kupacPonudaPopustDTO.setPonudaPopust(kupacPonudaPopust.getPonudaPopust() == null ? null : kupacPonudaPopust.getPonudaPopust().getPonudaPopustId());
        return kupacPonudaPopustDTO;
    }

    private KupacPonudaPopust mapToEntity(
            final KupacPonudaPopustDTO kupacPonudaPopustDTO,
            final KupacPonudaPopust kupacPonudaPopust) {
        kupacPonudaPopust.setKupacPonudaPopustFlag(kupacPonudaPopustDTO.getKupacPonudaPopustFlag());
        final Kupac kupac = kupacPonudaPopustDTO.getKupac() == null ? null : kupacRepository.findById(kupacPonudaPopustDTO.getKupac())
                .orElseThrow(() -> new NotFoundException("kupac not found"));
        kupacPonudaPopust.setKupac(kupac);
        final PonudaPopust ponudaPopust = kupacPonudaPopustDTO.getPonudaPopust() == null ? null : ponudaPopustRepository.findById(kupacPonudaPopustDTO.getPonudaPopust())
                .orElseThrow(() -> new NotFoundException("ponudaPopust not found"));
        kupacPonudaPopust.setPonudaPopust(ponudaPopust);
        return kupacPonudaPopust;
    }

}
