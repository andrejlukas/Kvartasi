package com.mojkvart.service;

import com.mojkvart.dtos.PonudaPopustDTO;
import com.mojkvart.entities.KorisnikTrgovinaPonuda;
import com.mojkvart.entities.Ponuda;
import com.mojkvart.entities.PonudaPopust;
import com.mojkvart.entities.Popust;
import com.mojkvart.repos.KorisnikTrgovinaPonudaRepository;
import com.mojkvart.repos.PonudaPopustRepository;
import com.mojkvart.repos.PonudaRepository;
import com.mojkvart.repos.PopustRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PonudaPopustService {

    private final PonudaPopustRepository ponudaPopustRepository;
    private final PonudaRepository ponudaRepository;
    private final PopustRepository popustRepository;
    private final KorisnikTrgovinaPonudaRepository korisnikTrgovinaPonudaRepository;

    public PonudaPopustService(final PonudaPopustRepository ponudaPopustRepository,
            final PonudaRepository ponudaRepository, final PopustRepository popustRepository,
            final KorisnikTrgovinaPonudaRepository korisnikTrgovinaPonudaRepository) {
        this.ponudaPopustRepository = ponudaPopustRepository;
        this.ponudaRepository = ponudaRepository;
        this.popustRepository = popustRepository;
        this.korisnikTrgovinaPonudaRepository = korisnikTrgovinaPonudaRepository;
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
        return ponudaPopustDTO;
    }

    private PonudaPopust mapToEntity(final PonudaPopustDTO ponudaPopustDTO,
            final PonudaPopust ponudaPopust) {
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
        final KorisnikTrgovinaPonuda ponudaPopustKorisnikTrgovinaPonuda = korisnikTrgovinaPonudaRepository.findFirstByPonudaPopust(ponudaPopust);
        if (ponudaPopustKorisnikTrgovinaPonuda != null) {
            referencedWarning.setKey("ponudaPopust.korisnikTrgovinaPonuda.ponudaPopust.referenced");
            referencedWarning.addParam(ponudaPopustKorisnikTrgovinaPonuda.getId());
            return referencedWarning;
        }
        return null;
    }

}
