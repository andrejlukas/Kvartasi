package com.mojkvart.service;

import com.mojkvart.dtos.OcjenaProizvodKorisnikDTO;
import com.mojkvart.entities.Korisnik;
import com.mojkvart.entities.Ocjena;
import com.mojkvart.entities.OcjenaProizvodKorisnik;
import com.mojkvart.entities.Proizvod;
import com.mojkvart.repos.KorisnikRepository;
import com.mojkvart.repos.OcjenaProizvodKorisnikRepository;
import com.mojkvart.repos.OcjenaRepository;
import com.mojkvart.repos.ProizvodRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OcjenaProizvodKorisnikService {

    private final OcjenaProizvodKorisnikRepository ocjenaProizvodKorisnikRepository;
    private final ProizvodRepository proizvodRepository;
    private final OcjenaRepository ocjenaRepository;
    private final KorisnikRepository korisnikRepository;

    public OcjenaProizvodKorisnikService(
            final OcjenaProizvodKorisnikRepository ocjenaProizvodKorisnikRepository,
            final ProizvodRepository proizvodRepository, final OcjenaRepository ocjenaRepository,
            final KorisnikRepository korisnikRepository) {
        this.ocjenaProizvodKorisnikRepository = ocjenaProizvodKorisnikRepository;
        this.proizvodRepository = proizvodRepository;
        this.ocjenaRepository = ocjenaRepository;
        this.korisnikRepository = korisnikRepository;
    }

    public List<OcjenaProizvodKorisnikDTO> findAll() {
        final List<OcjenaProizvodKorisnik> ocjenaProizvodKorisniks = ocjenaProizvodKorisnikRepository.findAll(Sort.by("id"));
        return ocjenaProizvodKorisniks.stream()
                .map(ocjenaProizvodKorisnik -> mapToDTO(ocjenaProizvodKorisnik, new OcjenaProizvodKorisnikDTO()))
                .toList();
    }

    public OcjenaProizvodKorisnikDTO get(final Long id) {
        return ocjenaProizvodKorisnikRepository.findById(id)
                .map(ocjenaProizvodKorisnik -> mapToDTO(ocjenaProizvodKorisnik, new OcjenaProizvodKorisnikDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OcjenaProizvodKorisnikDTO ocjenaProizvodKorisnikDTO) {
        final OcjenaProizvodKorisnik ocjenaProizvodKorisnik = new OcjenaProizvodKorisnik();
        mapToEntity(ocjenaProizvodKorisnikDTO, ocjenaProizvodKorisnik);
        return ocjenaProizvodKorisnikRepository.save(ocjenaProizvodKorisnik).getId();
    }

    public void update(final Long id, final OcjenaProizvodKorisnikDTO ocjenaProizvodKorisnikDTO) {
        final OcjenaProizvodKorisnik ocjenaProizvodKorisnik = ocjenaProizvodKorisnikRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ocjenaProizvodKorisnikDTO, ocjenaProizvodKorisnik);
        ocjenaProizvodKorisnikRepository.save(ocjenaProizvodKorisnik);
    }

    public void delete(final Long id) {
        ocjenaProizvodKorisnikRepository.deleteById(id);
    }

    private OcjenaProizvodKorisnikDTO mapToDTO(final OcjenaProizvodKorisnik ocjenaProizvodKorisnik,
            final OcjenaProizvodKorisnikDTO ocjenaProizvodKorisnikDTO) {
        ocjenaProizvodKorisnikDTO.setId(ocjenaProizvodKorisnik.getId());
        ocjenaProizvodKorisnikDTO.setProizvod(ocjenaProizvodKorisnik.getProizvod() == null ? null : ocjenaProizvodKorisnik.getProizvod().getProizvodId());
        ocjenaProizvodKorisnikDTO.setOcjena(ocjenaProizvodKorisnik.getOcjena() == null ? null : ocjenaProizvodKorisnik.getOcjena().getOcjenaId());
        ocjenaProizvodKorisnikDTO.setKorisnik(ocjenaProizvodKorisnik.getKorisnik() == null ? null : ocjenaProizvodKorisnik.getKorisnik().getKorisnikId());
        return ocjenaProizvodKorisnikDTO;
    }

    private OcjenaProizvodKorisnik mapToEntity(
            final OcjenaProizvodKorisnikDTO ocjenaProizvodKorisnikDTO,
            final OcjenaProizvodKorisnik ocjenaProizvodKorisnik) {
        final Proizvod proizvod = ocjenaProizvodKorisnikDTO.getProizvod() == null ? null : proizvodRepository.findById(ocjenaProizvodKorisnikDTO.getProizvod())
                .orElseThrow(() -> new NotFoundException("proizvod not found"));
        ocjenaProizvodKorisnik.setProizvod(proizvod);
        final Ocjena ocjena = ocjenaProizvodKorisnikDTO.getOcjena() == null ? null : ocjenaRepository.findById(ocjenaProizvodKorisnikDTO.getOcjena())
                .orElseThrow(() -> new NotFoundException("ocjena not found"));
        ocjenaProizvodKorisnik.setOcjena(ocjena);
        final Korisnik korisnik = ocjenaProizvodKorisnikDTO.getKorisnik() == null ? null : korisnikRepository.findById(ocjenaProizvodKorisnikDTO.getKorisnik())
                .orElseThrow(() -> new NotFoundException("korisnik not found"));
        ocjenaProizvodKorisnik.setKorisnik(korisnik);
        return ocjenaProizvodKorisnik;
    }

}
