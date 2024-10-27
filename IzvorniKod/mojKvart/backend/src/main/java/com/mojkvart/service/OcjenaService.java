package com.mojkvart.service;

import com.mojkvart.dtos.OcjenaDTO;
import com.mojkvart.entities.Ocjena;
import com.mojkvart.entities.OcjenaProizvodKorisnik;
import com.mojkvart.repos.OcjenaProizvodKorisnikRepository;
import com.mojkvart.repos.OcjenaRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OcjenaService {

    private final OcjenaRepository ocjenaRepository;
    private final OcjenaProizvodKorisnikRepository ocjenaProizvodKorisnikRepository;

    public OcjenaService(final OcjenaRepository ocjenaRepository,
            final OcjenaProizvodKorisnikRepository ocjenaProizvodKorisnikRepository) {
        this.ocjenaRepository = ocjenaRepository;
        this.ocjenaProizvodKorisnikRepository = ocjenaProizvodKorisnikRepository;
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
        final OcjenaProizvodKorisnik ocjenaOcjenaProizvodKorisnik = ocjenaProizvodKorisnikRepository.findFirstByOcjena(ocjena);
        if (ocjenaOcjenaProizvodKorisnik != null) {
            referencedWarning.setKey("ocjena.ocjenaProizvodKorisnik.ocjena.referenced");
            referencedWarning.addParam(ocjenaOcjenaProizvodKorisnik.getId());
            return referencedWarning;
        }
        return null;
    }

}
