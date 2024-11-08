package com.mojkvart.service;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacTrgovinaRecenzija;
import com.mojkvart.domain.Recenzija;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.model.KupacTrgovinaRecenzijaDTO;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.repos.KupacTrgovinaRecenzijaRepository;
import com.mojkvart.repos.RecenzijaRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class KupacTrgovinaRecenzijaService {

    private final KupacTrgovinaRecenzijaRepository kupacTrgovinaRecenzijaRepository;
    private final KupacRepository kupacRepository;
    private final TrgovinaRepository trgovinaRepository;
    private final RecenzijaRepository recenzijaRepository;

    public KupacTrgovinaRecenzijaService(
            final KupacTrgovinaRecenzijaRepository kupacTrgovinaRecenzijaRepository,
            final KupacRepository kupacRepository, final TrgovinaRepository trgovinaRepository,
            final RecenzijaRepository recenzijaRepository) {
        this.kupacTrgovinaRecenzijaRepository = kupacTrgovinaRecenzijaRepository;
        this.kupacRepository = kupacRepository;
        this.trgovinaRepository = trgovinaRepository;
        this.recenzijaRepository = recenzijaRepository;
    }

    public List<KupacTrgovinaRecenzijaDTO> findAll() {
        final List<KupacTrgovinaRecenzija> kupacTrgovinaRecenzijas = kupacTrgovinaRecenzijaRepository.findAll(Sort.by("id"));
        return kupacTrgovinaRecenzijas.stream()
                .map(kupacTrgovinaRecenzija -> mapToDTO(kupacTrgovinaRecenzija, new KupacTrgovinaRecenzijaDTO()))
                .toList();
    }

    public KupacTrgovinaRecenzijaDTO get(final Long id) {
        return kupacTrgovinaRecenzijaRepository.findById(id)
                .map(kupacTrgovinaRecenzija -> mapToDTO(kupacTrgovinaRecenzija, new KupacTrgovinaRecenzijaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final KupacTrgovinaRecenzijaDTO kupacTrgovinaRecenzijaDTO) {
        final KupacTrgovinaRecenzija kupacTrgovinaRecenzija = new KupacTrgovinaRecenzija();
        mapToEntity(kupacTrgovinaRecenzijaDTO, kupacTrgovinaRecenzija);
        return kupacTrgovinaRecenzijaRepository.save(kupacTrgovinaRecenzija).getId();
    }

    public void update(final Long id, final KupacTrgovinaRecenzijaDTO kupacTrgovinaRecenzijaDTO) {
        final KupacTrgovinaRecenzija kupacTrgovinaRecenzija = kupacTrgovinaRecenzijaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(kupacTrgovinaRecenzijaDTO, kupacTrgovinaRecenzija);
        kupacTrgovinaRecenzijaRepository.save(kupacTrgovinaRecenzija);
    }

    public void delete(final Long id) {
        kupacTrgovinaRecenzijaRepository.deleteById(id);
    }
   
    private KupacTrgovinaRecenzijaDTO mapToDTO(final KupacTrgovinaRecenzija kupacTrgovinaRecenzija,
            final KupacTrgovinaRecenzijaDTO kupacTrgovinaRecenzijaDTO) {
        kupacTrgovinaRecenzijaDTO.setId(kupacTrgovinaRecenzija.getId());
        kupacTrgovinaRecenzijaDTO.setOdobrioModerator(kupacTrgovinaRecenzija.getOdobrioModerator());
        kupacTrgovinaRecenzijaDTO.setKupac(kupacTrgovinaRecenzija.getKupac() == null ? null : kupacTrgovinaRecenzija.getKupac().getKupacId());
        kupacTrgovinaRecenzijaDTO.setTrgovina(kupacTrgovinaRecenzija.getTrgovina() == null ? null : kupacTrgovinaRecenzija.getTrgovina().getTrgovinaId());
        kupacTrgovinaRecenzijaDTO.setRecenzija(kupacTrgovinaRecenzija.getRecenzija() == null ? null : kupacTrgovinaRecenzija.getRecenzija().getRecenzijaId());
        return kupacTrgovinaRecenzijaDTO;
    }

    private KupacTrgovinaRecenzija mapToEntity(
            final KupacTrgovinaRecenzijaDTO kupacTrgovinaRecenzijaDTO,
            final KupacTrgovinaRecenzija kupacTrgovinaRecenzija) {
        kupacTrgovinaRecenzija.setOdobrioModerator(kupacTrgovinaRecenzijaDTO.getOdobrioModerator());
        final Kupac kupac = kupacTrgovinaRecenzijaDTO.getKupac() == null ? null : kupacRepository.findById(kupacTrgovinaRecenzijaDTO.getKupac())
                .orElseThrow(() -> new NotFoundException("kupac not found"));
        kupacTrgovinaRecenzija.setKupac(kupac);
        final Trgovina trgovina = kupacTrgovinaRecenzijaDTO.getTrgovina() == null ? null : trgovinaRepository.findById(kupacTrgovinaRecenzijaDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        kupacTrgovinaRecenzija.setTrgovina(trgovina);
        final Recenzija recenzija = kupacTrgovinaRecenzijaDTO.getRecenzija() == null ? null : recenzijaRepository.findById(kupacTrgovinaRecenzijaDTO.getRecenzija())
                .orElseThrow(() -> new NotFoundException("recenzija not found"));
        kupacTrgovinaRecenzija.setRecenzija(recenzija);
        return kupacTrgovinaRecenzija;
    }

}
