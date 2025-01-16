package com.mojkvart.service;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.Ponuda;
import com.mojkvart.domain.Popust;
import com.mojkvart.domain.Recenzija;
import com.mojkvart.domain.KupacPonudaPopust;
import com.mojkvart.domain.PonudaPopust;
import com.mojkvart.model.KupacPonudaPopustDTO;
import com.mojkvart.model.PonudaDTO;
import com.mojkvart.model.PonudaResponseDTO;
import com.mojkvart.model.PopustDTO;
import com.mojkvart.model.PopustResponseDTO;
import com.mojkvart.service.PonudaService;
import com.mojkvart.service.PopustService;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.repos.KupacPonudaPopustRepository;
import com.mojkvart.repos.PonudaRepository;
import com.mojkvart.repos.PopustRepository;
import com.mojkvart.repos.PonudaPopustRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class KupacPonudaPopustService {

    private final KupacPonudaPopustRepository kupacPonudaPopustRepository;
    private final KupacRepository kupacRepository;
    private final PonudaPopustRepository ponudaPopustRepository;
    private final PonudaRepository ponudaRepository;
    private final PopustRepository popustRepository;
    private final PonudaService ponudaService;
    private final PopustService popustService;

    public KupacPonudaPopustService(
            final KupacPonudaPopustRepository kupacPonudaPopustRepository,
            final KupacRepository kupacRepository, final TrgovinaRepository trgovinaRepository,
            final PonudaPopustRepository ponudaPopustRepository,
            final PonudaRepository ponudaRepository,
            final PopustRepository popustRepository,
            final PonudaService ponudaService,
            final PopustService popustService) {
        this.kupacPonudaPopustRepository = kupacPonudaPopustRepository;
        this.kupacRepository = kupacRepository;
        this.ponudaPopustRepository = ponudaPopustRepository;
        this.ponudaRepository = ponudaRepository;
        this.popustRepository = popustRepository;
        this.ponudaService = ponudaService;
        this.popustService = popustService;
    }

    public List<KupacPonudaPopustDTO> findAll() {
        final List<KupacPonudaPopust> kupacPonudaPopusts = kupacPonudaPopustRepository.findAll(Sort.by("id"));
        return kupacPonudaPopusts.stream()
                .map(kupacPonudaPopust -> mapToDTO(kupacPonudaPopust, new KupacPonudaPopustDTO()))
                .toList();
    }

    public List<PonudaResponseDTO> findAllUnusedPonudasForKupac(Integer kupacId) {
        List<KupacPonudaPopust> kupacPonudaPopusts = kupacPonudaPopustRepository.findAll(Sort.by("kupac"));

        List<PonudaPopust> ponudaPopusts = kupacPonudaPopusts.stream()
                .filter(kpp -> kpp.getKupac().getKupacId().equals(kupacId))
                .filter(kpp -> !kpp.getKupacPonudaPopustFlag()) // Filtriraj neiskorištene
                .map(KupacPonudaPopust::getPonudaPopust).toList();

        List<Ponuda> ponude = ponudaRepository.findAll().stream()
                .filter(p -> ponudaPopusts.contains(p.getPonudaPopust())).toList();

                return ponude.stream()
                .map(p -> {
                    PonudaResponseDTO dto = new PonudaResponseDTO();
                    PonudaDTO ponuda = ponudaService.mapToDTO(p, new PonudaDTO());

                    dto.setPonudaId(p.getPonudaId());
                    dto.setPonudaNaziv(p.getPonudaNaziv());
                    dto.setPonudaOpis(p.getPonudaOpis());
                    dto.setPonudaPopust(p.getPonudaPopust().getPonudaPopustId());
                    dto.setTrgovinaIme(ponuda.getTrgovinaIme());
                    dto.setKupacPonudaPopustId(
                        kupacPonudaPopustRepository.findAll().stream()
                            .filter(kpp -> kpp.getPonudaPopust() == p.getPonudaPopust()) // Provjeri odgovarajuću ponudu popusta
                            .map(KupacPonudaPopust::getId) // Dohvati ID
                            .findFirst() // Pronađi prvi odgovarajući ID
                            .orElse(null) // Ako ne postoji, postavi na null
                    );
                    return dto; // Vraća DTO iz lambde
                })
                .toList(); // Pretvara stream u listu
            
    }

    public List<PonudaResponseDTO> findAllUsedPonudasForKupac(Integer kupacId) {
        List<KupacPonudaPopust> kupacPonudaPopusts = kupacPonudaPopustRepository.findAll(Sort.by("kupac"));

        List<PonudaPopust> ponudaPopusts = kupacPonudaPopusts.stream()
                .filter(kpp -> kpp.getKupac().getKupacId().equals(kupacId))
                .filter(kpp -> kpp.getKupacPonudaPopustFlag()) // Filtriraj neiskorištene
                .map(KupacPonudaPopust::getPonudaPopust).toList();

        List<Ponuda> ponude = ponudaRepository.findAll().stream()
                .filter(p -> ponudaPopusts.contains(p.getPonudaPopust())).toList();

                return ponude.stream()
                .map(p -> {
                    PonudaDTO ponuda = ponudaService.mapToDTO(p, new PonudaDTO());

                    PonudaResponseDTO dto = new PonudaResponseDTO();
                    dto.setPonudaId(p.getPonudaId());
                    dto.setPonudaNaziv(p.getPonudaNaziv());
                    dto.setPonudaOpis(p.getPonudaOpis());
                    dto.setPonudaPopust(p.getPonudaPopust().getPonudaPopustId());
                    dto.setTrgovinaIme(ponuda.getTrgovinaIme());
                    dto.setKupacPonudaPopustId(
                        kupacPonudaPopustRepository.findAll().stream()
                            .filter(kpp -> kpp.getPonudaPopust() == p.getPonudaPopust()) // Provjeri odgovarajuću ponudu popusta
                            .map(KupacPonudaPopust::getId) // Dohvati ID
                            .findFirst() // Pronađi prvi odgovarajući ID
                            .orElse(null) // Ako ne postoji, postavi na null
                    );
                    return dto; // Vraća DTO iz lambde
                })
                .toList(); // Pretvara stream u listu
            
    }

    public List<PopustResponseDTO> findAllUnusedPopustsForKupac(Integer kupacId) {
        List<KupacPonudaPopust> kupacPonudaPopusts = kupacPonudaPopustRepository.findAll(Sort.by("kupac"));

        List<PonudaPopust> ponudaPopusts = kupacPonudaPopusts.stream()
                .filter(kpp -> kpp.getKupac().getKupacId().equals(kupacId))
                .filter(kpp -> !kpp.getKupacPonudaPopustFlag()) // Filtriraj neiskorištene
                .map(KupacPonudaPopust::getPonudaPopust).toList();

        List<Popust> popusti = popustRepository.findAll().stream()
                .filter(p -> ponudaPopusts.contains(p.getPonudaPopust())).toList();

                return popusti.stream()
                .map(p -> {
                    PopustDTO popust = popustService.mapToDTO(p, new PopustDTO());

                    PopustResponseDTO dto = new PopustResponseDTO();
                    dto.setPopustId(p.getPopustId());
                    dto.setPopustNaziv(p.getPopustNaziv());
                    dto.setPopustOpis(p.getPopustOpis());
                    dto.setPonudaPopust(p.getPonudaPopust().getPonudaPopustId());
                    dto.setTrgovinaIme(popust.getTrgovinaIme());
                    dto.setKupacPonudaPopustId(
                        kupacPonudaPopustRepository.findAll().stream()
                            .filter(kpp -> kpp.getPonudaPopust() == p.getPonudaPopust()) // Provjeri odgovarajuću ponudu popusta
                            .map(KupacPonudaPopust::getId) // Dohvati ID
                            .findFirst() // Pronađi prvi odgovarajući ID
                            .orElse(null) // Ako ne postoji, postavi na null
                    );
                    return dto; // Vraća DTO iz lambde
                })
                .toList(); // Pretvara stream u listu
            
    }

    public List<PopustResponseDTO> findAllUsedPopustsForKupac(Integer kupacId) {
        List<KupacPonudaPopust> kupacPonudaPopusts = kupacPonudaPopustRepository.findAll(Sort.by("kupac"));

        List<PonudaPopust> ponudaPopusts = kupacPonudaPopusts.stream()
                .filter(kpp -> kpp.getKupac().getKupacId().equals(kupacId))
                .filter(kpp -> kpp.getKupacPonudaPopustFlag()) // Filtriraj neiskorištene
                .map(KupacPonudaPopust::getPonudaPopust).toList();

        List<Popust> popusti = popustRepository.findAll().stream()
                .filter(p -> ponudaPopusts.contains(p.getPonudaPopust())).toList();

                return popusti.stream()
                .map(p -> {
                    PopustDTO popust = popustService.mapToDTO(p, new PopustDTO());

                    PopustResponseDTO dto = new PopustResponseDTO();
                    dto.setPopustId(p.getPopustId());
                    dto.setPopustNaziv(p.getPopustNaziv());
                    dto.setPopustOpis(p.getPopustOpis());
                    dto.setPonudaPopust(p.getPonudaPopust().getPonudaPopustId());
                    dto.setTrgovinaIme(popust.getTrgovinaIme());
                    dto.setKupacPonudaPopustId(
                        kupacPonudaPopustRepository.findAll().stream()
                            .filter(kpp -> kpp.getPonudaPopust() == p.getPonudaPopust()) // Provjeri odgovarajuću ponudu popusta
                            .map(KupacPonudaPopust::getId) // Dohvati ID
                            .findFirst() // Pronađi prvi odgovarajući ID
                            .orElse(null) // Ako ne postoji, postavi na null
                    );
                    return dto; // Vraća DTO iz lambde
                })
                .toList(); // Pretvara stream u listu
            
    }

    public KupacPonudaPopustDTO get(final Long id) {
        return kupacPonudaPopustRepository.findById(id)
                .map(kupacPonudaPopust -> mapToDTO(kupacPonudaPopust, new KupacPonudaPopustDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final KupacPonudaPopustDTO kupacPonudaPopustDTO) {
        final KupacPonudaPopust kupacPonudaPopust = new KupacPonudaPopust();
        mapToEntity(kupacPonudaPopustDTO, kupacPonudaPopust);
        return kupacPonudaPopustRepository.save(kupacPonudaPopust).getId();
    }

    public void update(final Long id,
            final KupacPonudaPopustDTO kupacPonudaPopustDTO) {
        final KupacPonudaPopust kupacPonudaPopust = kupacPonudaPopustRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(kupacPonudaPopustDTO, kupacPonudaPopust);
        kupacPonudaPopustRepository.save(kupacPonudaPopust);
    }

    public void promijeniIskoristeno(Integer kupacPonudaPopustId, Boolean novoStanje) {
        KupacPonudaPopust kupacPonudaPopust = kupacPonudaPopustRepository.findById(kupacPonudaPopustId);

        // Promijeni zastavicu 'iskoristeno' u true
        kupacPonudaPopust.setKupacPonudaPopustFlag(novoStanje);
        kupacPonudaPopustRepository.save(kupacPonudaPopust);
    }

    public void delete(final Long id) {
        kupacPonudaPopustRepository.deleteById(id);
    }

    private KupacPonudaPopustDTO mapToDTO(
            final KupacPonudaPopust kupacPonudaPopust,
            final KupacPonudaPopustDTO kupacPonudaPopustDTO) {
        kupacPonudaPopustDTO.setId(kupacPonudaPopust.getId());
        kupacPonudaPopustDTO.setKupacPonudaPopustFlag(kupacPonudaPopust.getKupacPonudaPopustFlag());
        kupacPonudaPopustDTO
                .setKupac(kupacPonudaPopust.getKupac() == null ? null : kupacPonudaPopust.getKupac().getKupacId());
        kupacPonudaPopustDTO.setPonudaPopust(kupacPonudaPopust.getPonudaPopust() == null ? null
                : kupacPonudaPopust.getPonudaPopust().getPonudaPopustId());
        return kupacPonudaPopustDTO;
    }

    private KupacPonudaPopust mapToEntity(
            final KupacPonudaPopustDTO kupacPonudaPopustDTO,
            final KupacPonudaPopust kupacPonudaPopust) {
        kupacPonudaPopust.setKupacPonudaPopustFlag(kupacPonudaPopustDTO.getKupacPonudaPopustFlag());
        final Kupac kupac = kupacPonudaPopustDTO.getKupac() == null ? null
                : kupacRepository.findById(kupacPonudaPopustDTO.getKupac())
                        .orElseThrow(() -> new NotFoundException("kupac not found"));
        kupacPonudaPopust.setKupac(kupac);
        final PonudaPopust ponudaPopust = kupacPonudaPopustDTO.getPonudaPopust() == null ? null
                : ponudaPopustRepository.findById(kupacPonudaPopustDTO.getPonudaPopust())
                        .orElseThrow(() -> new NotFoundException("ponudaPopust not found"));
        kupacPonudaPopust.setPonudaPopust(ponudaPopust);
        return kupacPonudaPopust;
    }

}
