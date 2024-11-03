package com.mojkvart.service;

import com.mojkvart.domain.KupacTrgovinaRecenzija;
import com.mojkvart.domain.Recenzija;
import com.mojkvart.model.RecenzijaDTO;
import com.mojkvart.repos.KupacTrgovinaRecenzijaRepository;
import com.mojkvart.repos.RecenzijaRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RecenzijaService {

    private final RecenzijaRepository recenzijaRepository;
    private final KupacTrgovinaRecenzijaRepository kupacTrgovinaRecenzijaRepository;

    public RecenzijaService(final RecenzijaRepository recenzijaRepository,
            final KupacTrgovinaRecenzijaRepository kupacTrgovinaRecenzijaRepository) {
        this.recenzijaRepository = recenzijaRepository;
        this.kupacTrgovinaRecenzijaRepository = kupacTrgovinaRecenzijaRepository;
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
        return recenzijaDTO;
    }

    private Recenzija mapToEntity(final RecenzijaDTO recenzijaDTO, final Recenzija recenzija) {
        recenzija.setRecenzijaOpis(recenzijaDTO.getRecenzijaOpis());
        recenzija.setRecenzijaZvjezdice(recenzijaDTO.getRecenzijaZvjezdice());
        recenzija.setRecenzijaOdgovor(recenzijaDTO.getRecenzijaOdgovor());
        return recenzija;
    }

    public ReferencedWarning getReferencedWarning(final Integer recenzijaId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Recenzija recenzija = recenzijaRepository.findById(recenzijaId)
                .orElseThrow(NotFoundException::new);
        final KupacTrgovinaRecenzija recenzijaKupacTrgovinaRecenzija = kupacTrgovinaRecenzijaRepository.findFirstByRecenzija(recenzija);
        if (recenzijaKupacTrgovinaRecenzija != null) {
            referencedWarning.setKey("recenzija.kupacTrgovinaRecenzija.recenzija.referenced");
            referencedWarning.addParam(recenzijaKupacTrgovinaRecenzija.getId());
            return referencedWarning;
        }
        return null;
    }

}
