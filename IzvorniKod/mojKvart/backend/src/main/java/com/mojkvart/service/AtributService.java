package com.mojkvart.service;

import com.mojkvart.domain.Atribut;
import com.mojkvart.model.AtributDTO;
import com.mojkvart.repos.AtributRepository;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class AtributService {

    private final AtributRepository atributRepository;
    private final TrgovinaRepository trgovinaRepository;

    public AtributService(final AtributRepository atributRepository,
            final TrgovinaRepository trgovinaRepository) {
        this.atributRepository = atributRepository;
        this.trgovinaRepository = trgovinaRepository;
    }

    public List<AtributDTO> findAll() {
        final List<Atribut> atributs = atributRepository.findAll(Sort.by("atributId"));
        return atributs.stream()
                .map(atribut -> mapToDTO(atribut, new AtributDTO()))
                .toList();
    }

    public AtributDTO get(final Integer atributId) {
        return atributRepository.findById(atributId)
                .map(atribut -> mapToDTO(atribut, new AtributDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AtributDTO atributDTO) {
        final Atribut atribut = new Atribut();
        mapToEntity(atributDTO, atribut);
        return atributRepository.save(atribut).getAtributId();
    }

    public void update(final Integer atributId, final AtributDTO atributDTO) {
        final Atribut atribut = atributRepository.findById(atributId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(atributDTO, atribut);
        atributRepository.save(atribut);
    }

    public void delete(final Integer atributId) {
        final Atribut atribut = atributRepository.findById(atributId)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        trgovinaRepository.findAllByImaAtributeAtributs(atribut)
                .forEach(trgovina -> trgovina.getImaAtributeAtributs().remove(atribut));
        atributRepository.delete(atribut);
    }

    private AtributDTO mapToDTO(final Atribut atribut, final AtributDTO atributDTO) {
        atributDTO.setAtributId(atribut.getAtributId());
        atributDTO.setAtributOpis(atribut.getAtributOpis());
        return atributDTO;
    }

    private Atribut mapToEntity(final AtributDTO atributDTO, final Atribut atribut) {
        atribut.setAtributOpis(atributDTO.getAtributOpis());
        return atribut;
    }

}
