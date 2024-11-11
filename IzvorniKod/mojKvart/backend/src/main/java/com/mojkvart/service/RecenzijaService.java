package com.mojkvart.service;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.Trgovina;
import com.mojkvart.domain.Recenzija;
import com.mojkvart.model.RecenzijaDTO;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.repos.RecenzijaRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecenzijaService {

    private final RecenzijaRepository recenzijaRepository;
    private final KupacRepository kupacRepository;
    private final TrgovinaRepository trgovinaRepository;

    public RecenzijaService(final RecenzijaRepository recenzijaRepository,
                            final KupacRepository kupacRepository,
                            final TrgovinaRepository trgovinaRepository) {
        this.recenzijaRepository = recenzijaRepository;
        this.kupacRepository = kupacRepository;
        this.trgovinaRepository = trgovinaRepository;
    }

    public List<RecenzijaDTO> findAll() {
        final List<Recenzija> recenzijas = recenzijaRepository.findAll(Sort.by("recenzijaId"));
        return recenzijas.stream()
                .map(recenzija -> mapToDTO(recenzija, new RecenzijaDTO()))
                .toList();
    }

    public RecenzijaDTO get(final Integer recenzijaId) {
        return recenzijaRepository.findById(recenzijaId)
                .map(recenzija -> mapToDTO(recenzija, new RecenzijaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final RecenzijaDTO recenzijaDTO) {
        final Recenzija recenzija = new Recenzija();
        mapToEntity(recenzijaDTO, recenzija);
        return recenzijaRepository.save(recenzija).getRecenzijaId();
    }

    public void update(final Integer recenzijaId, final RecenzijaDTO recenzijaDTO) {
        final Recenzija recenzija = recenzijaRepository.findById(recenzijaId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(recenzijaDTO, recenzija);
        recenzijaRepository.save(recenzija);
    }

    public void delete(final Integer recenzijaId) {
        recenzijaRepository.deleteById(recenzijaId);
    }

    private RecenzijaDTO mapToDTO(final Recenzija recenzija, final RecenzijaDTO recenzijaDTO) {
        recenzijaDTO.setRecenzijaId(recenzija.getRecenzijaId());
        recenzijaDTO.setRecenzijaOpis(recenzija.getRecenzijaOpis());
        recenzijaDTO.setRecenzijaZvjezdice(recenzija.getRecenzijaZvjezdice());
        recenzijaDTO.setRecenzijaOdgovor(recenzija.getRecenzijaOdgovor());
        recenzijaDTO.setVrijemeKreiranja(recenzija.getVrijemeKreiranja());
        recenzijaDTO.setKupacId(recenzija.getKupac().getKupacId());
        recenzijaDTO.setTrgovinaId(recenzija.getTrgovina().getTrgovinaId());
        return recenzijaDTO;
    }

    private Recenzija mapToEntity(final RecenzijaDTO recenzijaDTO, final Recenzija recenzija) {
        recenzija.setRecenzijaOpis(recenzijaDTO.getRecenzijaOpis());
        recenzija.setRecenzijaZvjezdice(recenzijaDTO.getRecenzijaZvjezdice());
        recenzija.setRecenzijaOdgovor(recenzijaDTO.getRecenzijaOdgovor());
        recenzija.setVrijemeKreiranja(recenzijaDTO.getVrijemeKreiranja());

        // Postavljanje entiteta Kupac i Trgovina
        Kupac kupac = kupacRepository.findById(recenzijaDTO.getKupacId())
                .orElseThrow(() -> new NotFoundException("Kupac not found"));
        recenzija.setKupac(kupac);

        Trgovina trgovina = trgovinaRepository.findById(recenzijaDTO.getTrgovinaId())
                .orElseThrow(() -> new NotFoundException("Trgovina not found"));
        recenzija.setTrgovina(trgovina);

        return recenzija;
    }

    public ReferencedWarning getReferencedWarning(final Integer recenzijaId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Recenzija recenzija = recenzijaRepository.findById(recenzijaId)
                .orElseThrow(NotFoundException::new);
        return null;
    }
}
