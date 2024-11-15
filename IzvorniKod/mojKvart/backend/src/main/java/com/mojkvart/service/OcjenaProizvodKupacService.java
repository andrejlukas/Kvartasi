package com.mojkvart.service;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.OcjenaProizvodKupac;
import com.mojkvart.domain.Proizvod;
import com.mojkvart.model.OcjenaProizvodKupacDTO;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.repos.OcjenaProizvodKupacRepository;
import com.mojkvart.repos.ProizvodRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OcjenaProizvodKupacService {

    private final OcjenaProizvodKupacRepository ocjenaProizvodKupacRepository;
    private final ProizvodRepository proizvodRepository;
    private final KupacRepository kupacRepository;

    public OcjenaProizvodKupacService(
            final OcjenaProizvodKupacRepository ocjenaProizvodKupacRepository,
            final ProizvodRepository proizvodRepository,
            final KupacRepository kupacRepository) {
        this.ocjenaProizvodKupacRepository = ocjenaProizvodKupacRepository;
        this.proizvodRepository = proizvodRepository;
        this.kupacRepository = kupacRepository;
    }

    public List<OcjenaProizvodKupacDTO> findAll() {
        final List<OcjenaProizvodKupac> ocjenaProizvodKupacs = ocjenaProizvodKupacRepository.findAll(Sort.by("id"));
        return ocjenaProizvodKupacs.stream()
                .map(ocjenaProizvodKupac -> mapToDTO(ocjenaProizvodKupac, new OcjenaProizvodKupacDTO()))
                .toList();
    }

    public OcjenaProizvodKupacDTO get(final Long id) {
        return ocjenaProizvodKupacRepository.findById(id)
                .map(ocjenaProizvodKupac -> mapToDTO(ocjenaProizvodKupac, new OcjenaProizvodKupacDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OcjenaProizvodKupacDTO ocjenaProizvodKupacDTO) {
        final OcjenaProizvodKupac ocjenaProizvodKupac = new OcjenaProizvodKupac();
        mapToEntity(ocjenaProizvodKupacDTO, ocjenaProizvodKupac);
        return ocjenaProizvodKupacRepository.save(ocjenaProizvodKupac).getId();
    }

    public void update(final Long id, final OcjenaProizvodKupacDTO ocjenaProizvodKupacDTO) {
        final OcjenaProizvodKupac ocjenaProizvodKupac = ocjenaProizvodKupacRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ocjenaProizvodKupacDTO, ocjenaProizvodKupac);
        ocjenaProizvodKupacRepository.save(ocjenaProizvodKupac);
    }

    public void delete(final Long id) {
        ocjenaProizvodKupacRepository.deleteById(id);
    }

    private OcjenaProizvodKupacDTO mapToDTO(final OcjenaProizvodKupac ocjenaProizvodKupac,
            final OcjenaProizvodKupacDTO ocjenaProizvodKupacDTO) {
        ocjenaProizvodKupacDTO.setId(ocjenaProizvodKupac.getId());
        ocjenaProizvodKupacDTO.setProizvod(ocjenaProizvodKupac.getProizvod() == null ? null : ocjenaProizvodKupac.getProizvod().getProizvodId());
        ocjenaProizvodKupacDTO.setOcjena(ocjenaProizvodKupac.getOcjena());
        ocjenaProizvodKupacDTO.setKupac(ocjenaProizvodKupac.getKupac() == null ? null : ocjenaProizvodKupac.getKupac().getKupacId());
        return ocjenaProizvodKupacDTO;
    }

    private OcjenaProizvodKupac mapToEntity(final OcjenaProizvodKupacDTO ocjenaProizvodKupacDTO,
            final OcjenaProizvodKupac ocjenaProizvodKupac) {
        final Proizvod proizvod = ocjenaProizvodKupacDTO.getProizvod() == null ? null : proizvodRepository.findById(ocjenaProizvodKupacDTO.getProizvod())
                .orElseThrow(() -> new NotFoundException("proizvod not found"));
        ocjenaProizvodKupac.setProizvod(proizvod);
        final Integer ocjena = ocjenaProizvodKupacDTO.getOcjena();
        ocjenaProizvodKupac.setOcjena(ocjena);
        final Kupac kupac = ocjenaProizvodKupacDTO.getKupac() == null ? null : kupacRepository.findById(ocjenaProizvodKupacDTO.getKupac())
                .orElseThrow(() -> new NotFoundException("kupac not found"));
        ocjenaProizvodKupac.setKupac(kupac);
        return ocjenaProizvodKupac;
    }

}
