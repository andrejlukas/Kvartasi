package com.mojkvart.service;

import com.mojkvart.dtos.KorisnikTrgovinaPonudaDTO;
import com.mojkvart.entities.Korisnik;
import com.mojkvart.entities.KorisnikTrgovinaPonuda;
import com.mojkvart.entities.PonudaPopust;
import com.mojkvart.entities.Trgovina;
import com.mojkvart.repos.KorisnikRepository;
import com.mojkvart.repos.KorisnikTrgovinaPonudaRepository;
import com.mojkvart.repos.PonudaPopustRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class KorisnikTrgovinaPonudaService {

    private final KorisnikTrgovinaPonudaRepository korisnikTrgovinaPonudaRepository;
    private final KorisnikRepository korisnikRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final PonudaPopustRepository ponudaPopustRepository;

    public KorisnikTrgovinaPonudaService(
            final KorisnikTrgovinaPonudaRepository korisnikTrgovinaPonudaRepository,
            final KorisnikRepository korisnikRepository,
            final TrgovinaRepository trgovinaRepository,
            final PonudaPopustRepository ponudaPopustRepository) {
        this.korisnikTrgovinaPonudaRepository = korisnikTrgovinaPonudaRepository;
        this.korisnikRepository = korisnikRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.ponudaPopustRepository = ponudaPopustRepository;
    }

    public List<KorisnikTrgovinaPonudaDTO> findAll() {
        final List<KorisnikTrgovinaPonuda> korisnikTrgovinaPonudas = korisnikTrgovinaPonudaRepository.findAll(Sort.by("id"));
        return korisnikTrgovinaPonudas.stream()
                .map(korisnikTrgovinaPonuda -> mapToDTO(korisnikTrgovinaPonuda, new KorisnikTrgovinaPonudaDTO()))
                .toList();
    }

    public KorisnikTrgovinaPonudaDTO get(final Long id) {
        return korisnikTrgovinaPonudaRepository.findById(id)
                .map(korisnikTrgovinaPonuda -> mapToDTO(korisnikTrgovinaPonuda, new KorisnikTrgovinaPonudaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final KorisnikTrgovinaPonudaDTO korisnikTrgovinaPonudaDTO) {
        final KorisnikTrgovinaPonuda korisnikTrgovinaPonuda = new KorisnikTrgovinaPonuda();
        mapToEntity(korisnikTrgovinaPonudaDTO, korisnikTrgovinaPonuda);
        return korisnikTrgovinaPonudaRepository.save(korisnikTrgovinaPonuda).getId();
    }

    public void update(final Long id, final KorisnikTrgovinaPonudaDTO korisnikTrgovinaPonudaDTO) {
        final KorisnikTrgovinaPonuda korisnikTrgovinaPonuda = korisnikTrgovinaPonudaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(korisnikTrgovinaPonudaDTO, korisnikTrgovinaPonuda);
        korisnikTrgovinaPonudaRepository.save(korisnikTrgovinaPonuda);
    }

    public void delete(final Long id) {
        korisnikTrgovinaPonudaRepository.deleteById(id);
    }

    private KorisnikTrgovinaPonudaDTO mapToDTO(final KorisnikTrgovinaPonuda korisnikTrgovinaPonuda,
            final KorisnikTrgovinaPonudaDTO korisnikTrgovinaPonudaDTO) {
        korisnikTrgovinaPonudaDTO.setId(korisnikTrgovinaPonuda.getId());
        korisnikTrgovinaPonudaDTO.setKorisnik(korisnikTrgovinaPonuda.getKorisnik() == null ? null : korisnikTrgovinaPonuda.getKorisnik().getKorisnikId());
        korisnikTrgovinaPonudaDTO.setTrgovina(korisnikTrgovinaPonuda.getTrgovina() == null ? null : korisnikTrgovinaPonuda.getTrgovina().getTrgovinaId());
        korisnikTrgovinaPonudaDTO.setPonudaPopust(korisnikTrgovinaPonuda.getPonudaPopust() == null ? null : korisnikTrgovinaPonuda.getPonudaPopust().getPonudaPopustId());
        return korisnikTrgovinaPonudaDTO;
    }

    private KorisnikTrgovinaPonuda mapToEntity(
            final KorisnikTrgovinaPonudaDTO korisnikTrgovinaPonudaDTO,
            final KorisnikTrgovinaPonuda korisnikTrgovinaPonuda) {
        final Korisnik korisnik = korisnikTrgovinaPonudaDTO.getKorisnik() == null ? null : korisnikRepository.findById(korisnikTrgovinaPonudaDTO.getKorisnik())
                .orElseThrow(() -> new NotFoundException("korisnik not found"));
        korisnikTrgovinaPonuda.setKorisnik(korisnik);
        final Trgovina trgovina = korisnikTrgovinaPonudaDTO.getTrgovina() == null ? null : trgovinaRepository.findById(korisnikTrgovinaPonudaDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        korisnikTrgovinaPonuda.setTrgovina(trgovina);
        final PonudaPopust ponudaPopust = korisnikTrgovinaPonudaDTO.getPonudaPopust() == null ? null : ponudaPopustRepository.findById(korisnikTrgovinaPonudaDTO.getPonudaPopust())
                .orElseThrow(() -> new NotFoundException("ponudaPopust not found"));
        korisnikTrgovinaPonuda.setPonudaPopust(ponudaPopust);
        return korisnikTrgovinaPonuda;
    }

}
