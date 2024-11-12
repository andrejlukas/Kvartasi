package com.mojkvart.service;

import com.mojkvart.domain.KupacProizvod;
import com.mojkvart.domain.OcjenaProizvodKupac;
import com.mojkvart.domain.Proizvod;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.ProizvodDTO;
import com.mojkvart.repos.KupacProizvodRepository;
import com.mojkvart.repos.OcjenaProizvodKupacRepository;
import com.mojkvart.repos.ProizvodRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProizvodService {

    private final ProizvodRepository proizvodRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final OcjenaProizvodKupacRepository ocjenaProizvodKupacRepository;
    private final KupacProizvodRepository kupacProizvodTrgovinaRepository;

    public ProizvodService(final ProizvodRepository proizvodRepository,
            final TrgovinaRepository trgovinaRepository,
            final OcjenaProizvodKupacRepository ocjenaProizvodKupacRepository,
            final KupacProizvodRepository kupacProizvodTrgovinaRepository) {
        this.proizvodRepository = proizvodRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.ocjenaProizvodKupacRepository = ocjenaProizvodKupacRepository;
        this.kupacProizvodTrgovinaRepository = kupacProizvodTrgovinaRepository;
    }

    public List<ProizvodDTO> findAll() {
        final List<Proizvod> proizvods = proizvodRepository.findAll(Sort.by("proizvodId"));
        return proizvods.stream()
                .map(proizvod -> mapToDTO(proizvod, new ProizvodDTO()))
                .toList();
    }

    public List<ProizvodDTO> findByTrgovina(Integer trgovinaId) {
        return proizvodRepository.findByTrgovinaId(trgovinaId).stream().map(p -> mapToDTO(p, new ProizvodDTO()))
                .toList();
    }

    public List<ProizvodDTO> findAllApproved() {
        return proizvodRepository.findAllApproved().stream().map(p -> mapToDTO(p, new ProizvodDTO())).toList();
    }

    public List<ProizvodDTO> findAllNotApproved() {
        return proizvodRepository.findAllNotApproved().stream().map(p -> mapToDTO(p, new ProizvodDTO())).toList();
    }

    public ProizvodDTO get(final Integer proizvodId) {
        return proizvodRepository.findById(proizvodId)
                .map(proizvod -> mapToDTO(proizvod, new ProizvodDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ProizvodDTO proizvodDTO) {
        final Proizvod proizvod = new Proizvod();
        mapToEntity(proizvodDTO, proizvod);
        return proizvodRepository.save(proizvod).getProizvodId();
    }

    public void update(final Integer proizvodId, final ProizvodDTO proizvodDTO) {
        final Proizvod proizvod = proizvodRepository.findById(proizvodId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(proizvodDTO, proizvod);
        proizvodRepository.save(proizvod);
    }

    public void delete(final Integer proizvodId) {
        proizvodRepository.deleteById(proizvodId);
    }

    private ProizvodDTO mapToDTO(final Proizvod proizvod, final ProizvodDTO proizvodDTO) {
        proizvodDTO.setProizvodId(proizvod.getProizvodId());
        proizvodDTO.setProizvodNaziv(proizvod.getProizvodNaziv());
        proizvodDTO.setProizvodOpis(proizvod.getProizvodOpis());
        proizvodDTO.setProizvodCijena(proizvod.getProizvodCijena());
        proizvodDTO.setProizvodKategorija(proizvod.getProizvodKategorija());
        proizvodDTO.setProizvodSlika(proizvod.getProizvodSlika());
        proizvodDTO.setProizvodFlag(proizvod.getProizvodFlag());
        proizvodDTO.setTrgovina(proizvod.getTrgovina() == null ? null : proizvod.getTrgovina().getTrgovinaId());
        return proizvodDTO;
    }

    private Proizvod mapToEntity(final ProizvodDTO proizvodDTO, final Proizvod proizvod) {
        proizvod.setProizvodNaziv(proizvodDTO.getProizvodNaziv());
        proizvod.setProizvodOpis(proizvodDTO.getProizvodOpis());
        proizvod.setProizvodCijena(proizvodDTO.getProizvodCijena());
        proizvod.setProizvodKategorija(proizvodDTO.getProizvodKategorija());
        proizvod.setProizvodSlika(proizvodDTO.getProizvodSlika());
        proizvod.setProizvodFlag(proizvodDTO.getProizvodFlag());
        final Trgovina trgovina = proizvodDTO.getTrgovina() == null ? null
                : trgovinaRepository.findById(proizvodDTO.getTrgovina())
                        .orElseThrow(() -> new NotFoundException("trgovina not found"));
        proizvod.setTrgovina(trgovina);
        return proizvod;
    }

    public ReferencedWarning getReferencedWarning(final Integer proizvodId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Proizvod proizvod = proizvodRepository.findById(proizvodId)
                .orElseThrow(NotFoundException::new);
        final OcjenaProizvodKupac proizvodOcjenaProizvodKupac = ocjenaProizvodKupacRepository
                .findFirstByProizvod(proizvod);
        if (proizvodOcjenaProizvodKupac != null) {
            referencedWarning.setKey("proizvod.ocjenaProizvodKupac.proizvod.referenced");
            referencedWarning.addParam(proizvodOcjenaProizvodKupac.getId());
            return referencedWarning;
        }
        final KupacProizvod proizvodKupacProizvodTrgovina = kupacProizvodTrgovinaRepository
                .findFirstByProizvod(proizvod);
        if (proizvodKupacProizvodTrgovina != null) {
            referencedWarning.setKey("proizvod.kupacProizvodTrgovina.proizvod.referenced");
            referencedWarning.addParam(proizvodKupacProizvodTrgovina.getId());
            return referencedWarning;
        }
        return null;
    }

}
