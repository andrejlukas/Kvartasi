package com.mojkvart.service;

import com.mojkvart.dtos.RecenzijaDTO;
import com.mojkvart.entities.KorisnikTrgovinaRecenzija;
import com.mojkvart.entities.Recenzija;
import com.mojkvart.repos.KorisnikTrgovinaRecenzijaRepository;
import com.mojkvart.repos.RecenzijaRepository;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RecenzijaService {

    private final RecenzijaRepository recenzijaRepository;
    private final KorisnikTrgovinaRecenzijaRepository korisnikTrgovinaRecenzijaRepository;

    public RecenzijaService(final RecenzijaRepository recenzijaRepository,
            final KorisnikTrgovinaRecenzijaRepository korisnikTrgovinaRecenzijaRepository) {
        this.recenzijaRepository = recenzijaRepository;
        this.korisnikTrgovinaRecenzijaRepository = korisnikTrgovinaRecenzijaRepository;
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
        return recenzijaDTO;
    }

    private Recenzija mapToEntity(final RecenzijaDTO recenzijaDTO, final Recenzija recenzija) {
        recenzija.setRecenzijaOpis(recenzijaDTO.getRecenzijaOpis());
        recenzija.setRecenzijaZvjezdice(recenzijaDTO.getRecenzijaZvjezdice());
        return recenzija;
    }

    public ReferencedWarning getReferencedWarning(final Integer recenzijaId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Recenzija recenzija = recenzijaRepository.findById(recenzijaId)
                .orElseThrow(NotFoundException::new);
        final KorisnikTrgovinaRecenzija recenzijaKorisnikTrgovinaRecenzija = korisnikTrgovinaRecenzijaRepository.findFirstByRecenzija(recenzija);
        if (recenzijaKorisnikTrgovinaRecenzija != null) {
            referencedWarning.setKey("recenzija.korisnikTrgovinaRecenzija.recenzija.referenced");
            referencedWarning.addParam(recenzijaKorisnikTrgovinaRecenzija.getId());
            return referencedWarning;
        }
        return null;
    }

}
