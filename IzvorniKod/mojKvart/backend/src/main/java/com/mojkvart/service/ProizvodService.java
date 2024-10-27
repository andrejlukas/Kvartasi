package com.mojkvart.service;

import com.mojkvart.dtos.ProizvodDTO;
import com.mojkvart.entities.OcjenaProizvodKorisnik;
import com.mojkvart.entities.Proizvod;
import com.mojkvart.repos.OcjenaProizvodKorisnikRepository;
import com.mojkvart.repos.ProizvodRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProizvodService {

    private final ProizvodRepository proizvodRepository;
    private final OcjenaProizvodKorisnikRepository ocjenaProizvodKorisnikRepository;

    public ProizvodService(final ProizvodRepository proizvodRepository,
            final OcjenaProizvodKorisnikRepository ocjenaProizvodKorisnikRepository) {
        this.proizvodRepository = proizvodRepository;
        this.ocjenaProizvodKorisnikRepository = ocjenaProizvodKorisnikRepository;
    }

    public List<ProizvodDTO> findAll() {
        final List<Proizvod> proizvods = proizvodRepository.findAll(Sort.by("proizvodId"));
        return proizvods.stream()
                .map(proizvod -> mapToDTO(proizvod, new ProizvodDTO()))
                .toList();
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
        return proizvodDTO;
    }

    private Proizvod mapToEntity(final ProizvodDTO proizvodDTO, final Proizvod proizvod) {
        proizvod.setProizvodNaziv(proizvodDTO.getProizvodNaziv());
        proizvod.setProizvodOpis(proizvodDTO.getProizvodOpis());
        proizvod.setProizvodCijena(proizvodDTO.getProizvodCijena());
        proizvod.setProizvodKategorija(proizvodDTO.getProizvodKategorija());
        proizvod.setProizvodSlika(proizvodDTO.getProizvodSlika());
        return proizvod;
    }

    public ReferencedWarning getReferencedWarning(final Integer proizvodId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Proizvod proizvod = proizvodRepository.findById(proizvodId)
                .orElseThrow(NotFoundException::new);
        final OcjenaProizvodKorisnik proizvodOcjenaProizvodKorisnik = ocjenaProizvodKorisnikRepository.findFirstByProizvod(proizvod);
        if (proizvodOcjenaProizvodKorisnik != null) {
            referencedWarning.setKey("proizvod.ocjenaProizvodKorisnik.proizvod.referenced");
            referencedWarning.addParam(proizvodOcjenaProizvodKorisnik.getId());
            return referencedWarning;
        }
        return null;
    }

}
