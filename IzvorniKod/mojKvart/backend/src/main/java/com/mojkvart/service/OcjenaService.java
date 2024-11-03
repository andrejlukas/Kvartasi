package com.mojkvart.service;

import com.mojkvart.domain.Ocjena;
import com.mojkvart.domain.OcjenaProizvodKupac;
import com.mojkvart.model.OcjenaDTO;
import com.mojkvart.repos.OcjenaProizvodKupacRepository;
import com.mojkvart.repos.OcjenaRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OcjenaService {

    private final OcjenaRepository ocjenaRepository;
    private final OcjenaProizvodKupacRepository ocjenaProizvodKupacRepository;

    public OcjenaService(final OcjenaRepository ocjenaRepository,
            final OcjenaProizvodKupacRepository ocjenaProizvodKupacRepository) {
        this.ocjenaRepository = ocjenaRepository;
        this.ocjenaProizvodKupacRepository = ocjenaProizvodKupacRepository;
    }

    public List<OcjenaDTO> findAll() {
        final List<Ocjena> ocjenas = ocjenaRepository.findAll(Sort.by("ocjenaId"));
        return ocjenas.stream()
                .map(ocjena -> mapToDTO(ocjena, new OcjenaDTO()))
                .toList();
    }

    public OcjenaDTO get(final Integer ocjenaId) {
        return ocjenaRepository.findById(ocjenaId)
                .map(ocjena -> mapToDTO(ocjena, new OcjenaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final OcjenaDTO ocjenaDTO) {
        final Ocjena ocjena = new Ocjena();
        mapToEntity(ocjenaDTO, ocjena);
        return ocjenaRepository.save(ocjena).getOcjenaId();
    }

    public void update(final Integer ocjenaId, final OcjenaDTO ocjenaDTO) {
        final Ocjena ocjena = ocjenaRepository.findById(ocjenaId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ocjenaDTO, ocjena);
        ocjenaRepository.save(ocjena);
    }

    public void delete(final Integer ocjenaId) {
        ocjenaRepository.deleteById(ocjenaId);
    }

    private OcjenaDTO mapToDTO(final Ocjena ocjena, final OcjenaDTO ocjenaDTO) {
        ocjenaDTO.setOcjenaId(ocjena.getOcjenaId());
        ocjenaDTO.setOcjenaZvjezdice(ocjena.getOcjenaZvjezdice());
        return ocjenaDTO;
    }

    private Ocjena mapToEntity(final OcjenaDTO ocjenaDTO, final Ocjena ocjena) {
        ocjena.setOcjenaZvjezdice(ocjenaDTO.getOcjenaZvjezdice());
        return ocjena;
    }

    public ReferencedWarning getReferencedWarning(final Integer ocjenaId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Ocjena ocjena = ocjenaRepository.findById(ocjenaId)
                .orElseThrow(NotFoundException::new);
        final OcjenaProizvodKupac ocjenaOcjenaProizvodKupac = ocjenaProizvodKupacRepository.findFirstByOcjena(ocjena);
        if (ocjenaOcjenaProizvodKupac != null) {
            referencedWarning.setKey("ocjena.ocjenaProizvodKupac.ocjena.referenced");
            referencedWarning.addParam(ocjenaOcjenaProizvodKupac.getId());
            return referencedWarning;
        }
        return null;
    }

}
