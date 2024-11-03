package com.mojkvart.service;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacProizvodTrgovina;
import com.mojkvart.domain.Proizvod;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.KupacProizvodTrgovinaDTO;
import com.mojkvart.repos.KupacProizvodTrgovinaRepository;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.repos.ProizvodRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class KupacProizvodTrgovinaService {

    private final KupacProizvodTrgovinaRepository kupacProizvodTrgovinaRepository;
    private final KupacRepository kupacRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final ProizvodRepository proizvodRepository;

    public KupacProizvodTrgovinaService(
            final KupacProizvodTrgovinaRepository kupacProizvodTrgovinaRepository,
            final KupacRepository kupacRepository, final TrgovinaRepository trgovinaRepository,
            final ProizvodRepository proizvodRepository) {
        this.kupacProizvodTrgovinaRepository = kupacProizvodTrgovinaRepository;
        this.kupacRepository = kupacRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.proizvodRepository = proizvodRepository;
    }

    public List<KupacProizvodTrgovinaDTO> findAll() {
        final List<KupacProizvodTrgovina> kupacProizvodTrgovinas = kupacProizvodTrgovinaRepository.findAll(Sort.by("id"));
        return kupacProizvodTrgovinas.stream()
                .map(kupacProizvodTrgovina -> mapToDTO(kupacProizvodTrgovina, new KupacProizvodTrgovinaDTO()))
                .toList();
    }

    public KupacProizvodTrgovinaDTO get(final Long id) {
        return kupacProizvodTrgovinaRepository.findById(id)
                .map(kupacProizvodTrgovina -> mapToDTO(kupacProizvodTrgovina, new KupacProizvodTrgovinaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final KupacProizvodTrgovinaDTO kupacProizvodTrgovinaDTO) {
        final KupacProizvodTrgovina kupacProizvodTrgovina = new KupacProizvodTrgovina();
        mapToEntity(kupacProizvodTrgovinaDTO, kupacProizvodTrgovina);
        return kupacProizvodTrgovinaRepository.save(kupacProizvodTrgovina).getId();
    }

    
    public void update(final Long id, final KupacProizvodTrgovinaDTO kupacProizvodTrgovinaDTO) {
        final KupacProizvodTrgovina kupacProizvodTrgovina = kupacProizvodTrgovinaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(kupacProizvodTrgovinaDTO, kupacProizvodTrgovina);
        kupacProizvodTrgovinaRepository.save(kupacProizvodTrgovina);
    }

    public void delete(final Long id) {
        kupacProizvodTrgovinaRepository.deleteById(id);
    }

    private KupacProizvodTrgovinaDTO mapToDTO(final KupacProizvodTrgovina kupacProizvodTrgovina,
            final KupacProizvodTrgovinaDTO kupacProizvodTrgovinaDTO) {
        kupacProizvodTrgovinaDTO.setId(kupacProizvodTrgovina.getId());
        kupacProizvodTrgovinaDTO.setKupacProizvodTrgovinaFlag(kupacProizvodTrgovina.getKupacProizvodTrgovinaFlag());
        kupacProizvodTrgovinaDTO.setKupac(kupacProizvodTrgovina.getKupac() == null ? null : kupacProizvodTrgovina.getKupac().getKupacId());
        kupacProizvodTrgovinaDTO.setTrgovina(kupacProizvodTrgovina.getTrgovina() == null ? null : kupacProizvodTrgovina.getTrgovina().getTrgovinaId());
        kupacProizvodTrgovinaDTO.setProizvod(kupacProizvodTrgovina.getProizvod() == null ? null : kupacProizvodTrgovina.getProizvod().getProizvodId());
        return kupacProizvodTrgovinaDTO;
    }

    private KupacProizvodTrgovina mapToEntity(
            final KupacProizvodTrgovinaDTO kupacProizvodTrgovinaDTO,
            final KupacProizvodTrgovina kupacProizvodTrgovina) {
        kupacProizvodTrgovina.setKupacProizvodTrgovinaFlag(kupacProizvodTrgovinaDTO.getKupacProizvodTrgovinaFlag());
        final Kupac kupac = kupacProizvodTrgovinaDTO.getKupac() == null ? null : kupacRepository.findById(kupacProizvodTrgovinaDTO.getKupac())
                .orElseThrow(() -> new NotFoundException("kupac not found"));
        kupacProizvodTrgovina.setKupac(kupac);
        final Trgovina trgovina = kupacProizvodTrgovinaDTO.getTrgovina() == null ? null : trgovinaRepository.findById(kupacProizvodTrgovinaDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        kupacProizvodTrgovina.setTrgovina(trgovina);
        final Proizvod proizvod = kupacProizvodTrgovinaDTO.getProizvod() == null ? null : proizvodRepository.findById(kupacProizvodTrgovinaDTO.getProizvod())
                .orElseThrow(() -> new NotFoundException("proizvod not found"));
        kupacProizvodTrgovina.setProizvod(proizvod);
        return kupacProizvodTrgovina;
    }

}
