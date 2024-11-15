package com.mojkvart.service;

import com.mojkvart.domain.Ponuda;
import com.mojkvart.domain.PonudaPopust;
import com.mojkvart.model.PonudaDTO;
import com.mojkvart.repos.PonudaPopustRepository;
import com.mojkvart.repos.PonudaRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PonudaService {

    private final PonudaRepository ponudaRepository;
    private final PonudaPopustRepository ponudaPopustRepository;

    public PonudaService(final PonudaRepository ponudaRepository,
            final PonudaPopustRepository ponudaPopustRepository) {
        this.ponudaRepository = ponudaRepository;
        this.ponudaPopustRepository = ponudaPopustRepository;
    }

    public List<PonudaDTO> findAll() {
        final List<Ponuda> ponudas = ponudaRepository.findAll(Sort.by("ponudaId"));
        return ponudas.stream()
                .map(ponuda -> mapToDTO(ponuda, new PonudaDTO()))
                .toList();
    }

    public PonudaDTO get(final Integer ponudaId) {
        return ponudaRepository.findById(ponudaId)
                .map(ponuda -> mapToDTO(ponuda, new PonudaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PonudaDTO ponudaDTO) {
        final Ponuda ponuda = new Ponuda();
        mapToEntity(ponudaDTO, ponuda);
        return ponudaRepository.save(ponuda).getPonudaId();
    }

    public void update(final Integer ponudaId, final PonudaDTO ponudaDTO) {
        final Ponuda ponuda = ponudaRepository.findById(ponudaId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ponudaDTO, ponuda);
        ponudaRepository.save(ponuda);
    }

    public void delete(final Integer ponudaId) {
        ponudaRepository.deleteById(ponudaId);
    }

    private PonudaDTO mapToDTO(final Ponuda ponuda, final PonudaDTO ponudaDTO) {
        ponudaDTO.setPonudaId(ponuda.getPonudaId());
        ponudaDTO.setPonudaNaziv(ponuda.getPonudaNaziv());
        ponudaDTO.setPonudaOpis(ponuda.getPonudaOpis());
        ponudaDTO.setPonudaPopust(ponuda.getPonudaPopust() == null ? null : ponuda.getPonudaPopust().getPonudaPopustId());
        return ponudaDTO;
    }

    private Ponuda mapToEntity(final PonudaDTO ponudaDTO, final Ponuda ponuda) {
        ponuda.setPonudaNaziv(ponudaDTO.getPonudaNaziv());
        ponuda.setPonudaOpis(ponudaDTO.getPonudaOpis());
        final PonudaPopust ponudaPopust = ponudaDTO.getPonudaPopust() == null ? null : ponudaPopustRepository.findById(ponudaDTO.getPonudaPopust())
                .orElseThrow(() -> new NotFoundException("ponudaPopust not found"));
        ponuda.setPonudaPopust(ponudaPopust);
        return ponuda;
    }

}
