package com.mojkvart.service;

import com.mojkvart.domain.KupacPonudaPopust;
import com.mojkvart.domain.Ponuda;
import com.mojkvart.domain.PonudaPopust;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.PonudaDTO;
import com.mojkvart.repos.KupacPonudaPopustRepository;
import com.mojkvart.repos.PonudaPopustRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.repos.PonudaRepository;
import com.mojkvart.util.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PonudaService {

    private final PonudaRepository ponudaRepository;
    private final PonudaPopustRepository ponudaPopustRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final KupacPonudaPopustRepository kupacPonudaPopustRepository;

    public PonudaService(final PonudaRepository ponudaRepository,
            final PonudaPopustRepository ponudaPopustRepository,
            final TrgovinaRepository trgovinaRepository, final KupacPonudaPopustRepository kupacPonudaPopustRepository) {
        this.ponudaRepository = ponudaRepository;
        this.ponudaPopustRepository = ponudaPopustRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.kupacPonudaPopustRepository = kupacPonudaPopustRepository;
    }

    // vraća sve ponude koje su odobrene od strane moderatora i nisu istekle i koje kupac već nije spremio
    public List<PonudaDTO> findAllWithFlagTrue(Integer kupacId) {
        final List<Ponuda> ponudas = ponudaRepository.findAll(Sort.by("ponudaId"));
        final List<KupacPonudaPopust> kupacPonudaPopusts = kupacPonudaPopustRepository.findAll(Sort.by("kupac"));

        // Pronađi sve ponudaPopuste koje je kupac već spremio
        final Set<Integer> spremljenePonudeIds = kupacPonudaPopusts.stream()
                .filter(kpp -> kpp.getKupac().getKupacId().equals(kupacId))
                .map(kpp -> kpp.getPonudaPopust().getPonudaPopustId())
                .collect(Collectors.toSet());

        // Filtriraj ponude koje imaju flag true i nisu spremljene od strane kupca
        return ponudas.stream()
                .filter(ponuda -> ponuda.getPonudaPopust() != null &&
                        ponuda.getPonudaPopust().getPonudaPopustFlag() != null &&
                        ponuda.getPonudaPopust().getPonudaPopustFlag() &&
                        ponuda.getPonudaRok().isAfter(LocalDateTime.now()) &&
                        !spremljenePonudeIds.contains(ponuda.getPonudaPopust().getPonudaPopustId()))
                .map(ponuda -> mapToDTO(ponuda, new PonudaDTO()))
                .toList();
    }


    // vraća sve ponude koje moderator mora pregledati, a da nisu istekle
    public List<PonudaDTO> findAllWithFlagFalse() {
        final List<Ponuda> ponudas = ponudaRepository.findAll(Sort.by("ponudaId"));
        return ponudas.stream()
                .filter(ponuda -> ponuda.getPonudaPopust() != null &&
                        ponuda.getPonudaPopust().getPonudaPopustFlag() != null &&
                        ponuda.getPonudaRok().isAfter(LocalDateTime.now()) &&
                        !ponuda.getPonudaPopust().getPonudaPopustFlag())
                .map(ponuda -> mapToDTO(ponuda, new PonudaDTO()))
                .toList();
    }

    // vraća sve ponude koje je moderator odobrio, a da nisu istekle
    public List<PonudaDTO> findAllWithFlagTrue() {
        final List<Ponuda> ponudas = ponudaRepository.findAll(Sort.by("ponudaId"));
        return ponudas.stream()
                .filter(ponuda -> ponuda.getPonudaPopust() != null &&
                        ponuda.getPonudaPopust().getPonudaPopustFlag() != null &&
                        ponuda.getPonudaRok().isAfter(LocalDateTime.now()) &&
                        ponuda.getPonudaPopust().getPonudaPopustFlag() &&
                        ponuda.getPonudaPopust().getTrgovina().getTrgovinaStatus().equals("V"))
                .map(ponuda -> mapToDTO(ponuda, new PonudaDTO()))
                .toList();
    }


    public List<PonudaDTO> findAllTrgovinaValidPonudas(Integer trgovinaId) {
        final List<Ponuda> ponudas = ponudaRepository.findAll(Sort.by("ponudaId"));
        return ponudas.stream()
                .filter(ponuda -> ponuda.getPonudaPopust() != null &&
                        ponuda.getPonudaPopust().getPonudaPopustFlag() != null &&
                        Objects.equals(ponuda.getPonudaPopust().getTrgovina().getTrgovinaId(), trgovinaId) &&
                        ponuda.getPonudaPopust().getPonudaPopustFlag() &&
                        ponuda.getPonudaRok().isAfter(LocalDateTime.now()))
                .map(ponuda -> mapToDTO(ponuda, new PonudaDTO()))
                .toList();
    }

    public List<PonudaDTO> findAllTrgovinaNonValidPonudas(Integer trgovinaId) {
        final List<Ponuda> ponudas = ponudaRepository.findAll(Sort.by("ponudaId"));
        return ponudas.stream()
                .filter(ponuda -> ponuda.getPonudaPopust() != null &&
                        ponuda.getPonudaPopust().getPonudaPopustFlag() != null &&
                        Objects.equals(ponuda.getPonudaPopust().getTrgovina().getTrgovinaId(), trgovinaId) &&
                        (!ponuda.getPonudaPopust().getPonudaPopustFlag() || ponuda.getPonudaRok().isBefore(LocalDateTime.now())))
                .map(ponuda -> mapToDTO(ponuda, new PonudaDTO()))
                .toList();
    }

    public PonudaDTO get(final Integer ponudaId) {
         Ponuda ponuda = ponudaRepository.findById(ponudaId)
                .orElseThrow(() -> new RuntimeException("Ponuda nije pronađena"));

        PonudaDTO ponudaDTO = new PonudaDTO();
        mapToDTO(ponuda, ponudaDTO);
        return ponudaDTO;
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

    PonudaDTO mapToDTO(final Ponuda ponuda, final PonudaDTO ponudaDTO) {
        ponudaDTO.setPonudaId(ponuda.getPonudaId());
        ponudaDTO.setPonudaNaziv(ponuda.getPonudaNaziv());
        ponudaDTO.setPonudaOpis(ponuda.getPonudaOpis());
        ponudaDTO.setPonudaRok(PopustService.setVrijeme(ponuda.getPonudaRok()));
        ponudaDTO.setPonudaPopust(ponuda.getPonudaPopust() == null ? null : ponuda.getPonudaPopust().getPonudaPopustId());
        // Dohvati PonudaPopust objekt preko ponudaPopust ID-a
        PonudaPopust ponudaPopust = ponuda.getPonudaPopust();

        // Dohvati Trgovina objekt preko trgovina ID-a iz PonudaPopust
        Trgovina trgovina = trgovinaRepository.findById(ponudaPopust.getTrgovina().getTrgovinaId())
                .orElseThrow(() -> new RuntimeException("Trgovina nije pronađena"));

        // Dodaj ime trgovine u Ponuda objekt (ako Ponuda ima dodatno polje za trgovinu)
        ponuda.setTrgovinaIme(trgovina.getTrgovinaNaziv());
        ponudaDTO.setTrgovinaIme(ponuda.getTrgovinaIme());
        return ponudaDTO;
    }

    private Ponuda mapToEntity(final PonudaDTO ponudaDTO, final Ponuda ponuda) {
        ponuda.setPonudaNaziv(ponudaDTO.getPonudaNaziv());
        ponuda.setPonudaOpis(ponudaDTO.getPonudaOpis());
        ponuda.setPonudaRok(DogadajService.getVrijeme(ponudaDTO.getPonudaRok()));
        final PonudaPopust ponudaPopust = ponudaDTO.getPonudaPopust() == null ? null : ponudaPopustRepository.findById(ponudaDTO.getPonudaPopust())
                .orElseThrow(() -> new NotFoundException("ponudaPopust not found"));
        ponuda.setPonudaPopust(ponudaPopust);
        return ponuda;
    }

}
