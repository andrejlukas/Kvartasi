package com.mojkvart.service;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacTrgovinaPonudaPopust;
import com.mojkvart.domain.PonudaPopust;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.KupacTrgovinaPonudaPopustDTO;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.repos.KupacTrgovinaPonudaPopustRepository;
import com.mojkvart.repos.PonudaPopustRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class KupacTrgovinaPonudaPopustService {

    private final KupacTrgovinaPonudaPopustRepository kupacTrgovinaPonudaPopustRepository;
    private final KupacRepository kupacRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final PonudaPopustRepository ponudaPopustRepository;

    public KupacTrgovinaPonudaPopustService(
            final KupacTrgovinaPonudaPopustRepository kupacTrgovinaPonudaPopustRepository,
            final KupacRepository kupacRepository, final TrgovinaRepository trgovinaRepository,
            final PonudaPopustRepository ponudaPopustRepository) {
        this.kupacTrgovinaPonudaPopustRepository = kupacTrgovinaPonudaPopustRepository;
        this.kupacRepository = kupacRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.ponudaPopustRepository = ponudaPopustRepository;
    }

    public List<KupacTrgovinaPonudaPopustDTO> findAll() {
        final List<KupacTrgovinaPonudaPopust> kupacTrgovinaPonudaPopusts = kupacTrgovinaPonudaPopustRepository.findAll(Sort.by("id"));
        return kupacTrgovinaPonudaPopusts.stream()
                .map(kupacTrgovinaPonudaPopust -> mapToDTO(kupacTrgovinaPonudaPopust, new KupacTrgovinaPonudaPopustDTO()))
                .toList();
    }

    public KupacTrgovinaPonudaPopustDTO get(final Long id) {
        return kupacTrgovinaPonudaPopustRepository.findById(id)
                .map(kupacTrgovinaPonudaPopust -> mapToDTO(kupacTrgovinaPonudaPopust, new KupacTrgovinaPonudaPopustDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final KupacTrgovinaPonudaPopustDTO kupacTrgovinaPonudaPopustDTO) {
        final KupacTrgovinaPonudaPopust kupacTrgovinaPonudaPopust = new KupacTrgovinaPonudaPopust();
        mapToEntity(kupacTrgovinaPonudaPopustDTO, kupacTrgovinaPonudaPopust);
        return kupacTrgovinaPonudaPopustRepository.save(kupacTrgovinaPonudaPopust).getId();
    }

    public void update(final Long id,
            final KupacTrgovinaPonudaPopustDTO kupacTrgovinaPonudaPopustDTO) {
        final KupacTrgovinaPonudaPopust kupacTrgovinaPonudaPopust = kupacTrgovinaPonudaPopustRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(kupacTrgovinaPonudaPopustDTO, kupacTrgovinaPonudaPopust);
        kupacTrgovinaPonudaPopustRepository.save(kupacTrgovinaPonudaPopust);
    }

    public void delete(final Long id) {
        kupacTrgovinaPonudaPopustRepository.deleteById(id);
    }

    private KupacTrgovinaPonudaPopustDTO mapToDTO(
            final KupacTrgovinaPonudaPopust kupacTrgovinaPonudaPopust,
            final KupacTrgovinaPonudaPopustDTO kupacTrgovinaPonudaPopustDTO) {
        kupacTrgovinaPonudaPopustDTO.setId(kupacTrgovinaPonudaPopust.getId());
        kupacTrgovinaPonudaPopustDTO.setKupacTrgovinaPonudaPopustFlag(kupacTrgovinaPonudaPopust.getKupacTrgovinaPonudaPopustFlag());
        kupacTrgovinaPonudaPopustDTO.setKupac(kupacTrgovinaPonudaPopust.getKupac() == null ? null : kupacTrgovinaPonudaPopust.getKupac().getKupacId());
        kupacTrgovinaPonudaPopustDTO.setTrgovina(kupacTrgovinaPonudaPopust.getTrgovina() == null ? null : kupacTrgovinaPonudaPopust.getTrgovina().getTrgovinaId());
        kupacTrgovinaPonudaPopustDTO.setPonudaPopust(kupacTrgovinaPonudaPopust.getPonudaPopust() == null ? null : kupacTrgovinaPonudaPopust.getPonudaPopust().getPonudaPopustId());
        return kupacTrgovinaPonudaPopustDTO;
    }

    private KupacTrgovinaPonudaPopust mapToEntity(
            final KupacTrgovinaPonudaPopustDTO kupacTrgovinaPonudaPopustDTO,
            final KupacTrgovinaPonudaPopust kupacTrgovinaPonudaPopust) {
        kupacTrgovinaPonudaPopust.setKupacTrgovinaPonudaPopustFlag(kupacTrgovinaPonudaPopustDTO.getKupacTrgovinaPonudaPopustFlag());
        final Kupac kupac = kupacTrgovinaPonudaPopustDTO.getKupac() == null ? null : kupacRepository.findById(kupacTrgovinaPonudaPopustDTO.getKupac())
                .orElseThrow(() -> new NotFoundException("kupac not found"));
        kupacTrgovinaPonudaPopust.setKupac(kupac);
        final Trgovina trgovina = kupacTrgovinaPonudaPopustDTO.getTrgovina() == null ? null : trgovinaRepository.findById(kupacTrgovinaPonudaPopustDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        kupacTrgovinaPonudaPopust.setTrgovina(trgovina);
        final PonudaPopust ponudaPopust = kupacTrgovinaPonudaPopustDTO.getPonudaPopust() == null ? null : ponudaPopustRepository.findById(kupacTrgovinaPonudaPopustDTO.getPonudaPopust())
                .orElseThrow(() -> new NotFoundException("ponudaPopust not found"));
        kupacTrgovinaPonudaPopust.setPonudaPopust(ponudaPopust);
        return kupacTrgovinaPonudaPopust;
    }

}
