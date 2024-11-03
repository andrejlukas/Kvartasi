package com.mojkvart.service;

import com.mojkvart.domain.KupacTrgovinaPonudaPopust;
import com.mojkvart.domain.Ponuda;
import com.mojkvart.domain.PonudaPopust;
import com.mojkvart.domain.Popust;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.PonudaPopustDTO;
import com.mojkvart.repos.KupacTrgovinaPonudaPopustRepository;
import com.mojkvart.repos.PonudaPopustRepository;
import com.mojkvart.repos.PonudaRepository;
import com.mojkvart.repos.PopustRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PonudaPopustService {

    private final PonudaPopustRepository ponudaPopustRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final PonudaRepository ponudaRepository;
    private final PopustRepository popustRepository;
    private final KupacTrgovinaPonudaPopustRepository kupacTrgovinaPonudaPopustRepository;

    public PonudaPopustService(final PonudaPopustRepository ponudaPopustRepository,
            final TrgovinaRepository trgovinaRepository, final PonudaRepository ponudaRepository,
            final PopustRepository popustRepository,
            final KupacTrgovinaPonudaPopustRepository kupacTrgovinaPonudaPopustRepository) {
        this.ponudaPopustRepository = ponudaPopustRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.ponudaRepository = ponudaRepository;
        this.popustRepository = popustRepository;
        this.kupacTrgovinaPonudaPopustRepository = kupacTrgovinaPonudaPopustRepository;
    }

    public List<PonudaPopustDTO> findAll() {
        final List<PonudaPopust> ponudaPopusts = ponudaPopustRepository.findAll(Sort.by("ponudaPopustId"));
        return ponudaPopusts.stream()
                .map(ponudaPopust -> mapToDTO(ponudaPopust, new PonudaPopustDTO()))
                .toList();
    }

    public PonudaPopustDTO get(final Integer ponudaPopustId) {
        return ponudaPopustRepository.findById(ponudaPopustId)
                .map(ponudaPopust -> mapToDTO(ponudaPopust, new PonudaPopustDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PonudaPopustDTO ponudaPopustDTO) {
        final PonudaPopust ponudaPopust = new PonudaPopust();
        mapToEntity(ponudaPopustDTO, ponudaPopust);
        return ponudaPopustRepository.save(ponudaPopust).getPonudaPopustId();
    }

    public void update(final Integer ponudaPopustId, final PonudaPopustDTO ponudaPopustDTO) {
        final PonudaPopust ponudaPopust = ponudaPopustRepository.findById(ponudaPopustId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ponudaPopustDTO, ponudaPopust);
        ponudaPopustRepository.save(ponudaPopust);
    }

    public void delete(final Integer ponudaPopustId) {
        ponudaPopustRepository.deleteById(ponudaPopustId);
    }

    private PonudaPopustDTO mapToDTO(final PonudaPopust ponudaPopust,
            final PonudaPopustDTO ponudaPopustDTO) {
        ponudaPopustDTO.setPonudaPopustId(ponudaPopust.getPonudaPopustId());
        ponudaPopustDTO.setPonudaPopustFlag(ponudaPopust.getPonudaPopustFlag());
        ponudaPopustDTO.setTrgovina(ponudaPopust.getTrgovina() == null ? null : ponudaPopust.getTrgovina().getTrgovinaId());
        return ponudaPopustDTO;
    }

    private PonudaPopust mapToEntity(final PonudaPopustDTO ponudaPopustDTO,
            final PonudaPopust ponudaPopust) {
        ponudaPopust.setPonudaPopustFlag(ponudaPopustDTO.getPonudaPopustFlag());
        final Trgovina trgovina = ponudaPopustDTO.getTrgovina() == null ? null : trgovinaRepository.findById(ponudaPopustDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        ponudaPopust.setTrgovina(trgovina);
        return ponudaPopust;
    }

    public ReferencedWarning getReferencedWarning(final Integer ponudaPopustId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final PonudaPopust ponudaPopust = ponudaPopustRepository.findById(ponudaPopustId)
                .orElseThrow(NotFoundException::new);
        final Ponuda ponudaPopustPonuda = ponudaRepository.findFirstByPonudaPopust(ponudaPopust);
        if (ponudaPopustPonuda != null) {
            referencedWarning.setKey("ponudaPopust.ponuda.ponudaPopust.referenced");
            referencedWarning.addParam(ponudaPopustPonuda.getPonudaId());
            return referencedWarning;
        }
        final Popust ponudaPopustPopust = popustRepository.findFirstByPonudaPopust(ponudaPopust);
        if (ponudaPopustPopust != null) {
            referencedWarning.setKey("ponudaPopust.popust.ponudaPopust.referenced");
            referencedWarning.addParam(ponudaPopustPopust.getPopustId());
            return referencedWarning;
        }
        final KupacTrgovinaPonudaPopust ponudaPopustKupacTrgovinaPonudaPopust = kupacTrgovinaPonudaPopustRepository.findFirstByPonudaPopust(ponudaPopust);
        if (ponudaPopustKupacTrgovinaPonudaPopust != null) {
            referencedWarning.setKey("ponudaPopust.kupacTrgovinaPonudaPopust.ponudaPopust.referenced");
            referencedWarning.addParam(ponudaPopustKupacTrgovinaPonudaPopust.getId());
            return referencedWarning;
        }
        return null;
    }

}
