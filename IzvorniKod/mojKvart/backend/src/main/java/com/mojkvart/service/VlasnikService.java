package com.mojkvart.service;

import com.mojkvart.domain.Trgovina;
import com.mojkvart.domain.Vlasnik;
import com.mojkvart.model.VlasnikDTO;
import com.mojkvart.repos.TrgovinaRepository;
import com.mojkvart.repos.VlasnikRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VlasnikService {

    private final VlasnikRepository vlasnikRepository;
    private final TrgovinaRepository trgovinaRepository;

    public VlasnikService(final VlasnikRepository vlasnikRepository,
            final TrgovinaRepository trgovinaRepository) {
        this.vlasnikRepository = vlasnikRepository;
        this.trgovinaRepository = trgovinaRepository;
    }

    public List<VlasnikDTO> findAll() {
        final List<Vlasnik> vlasniks = vlasnikRepository.findAll(Sort.by("vlasnikId"));
        return vlasniks.stream()
                .map(vlasnik -> mapToDTO(vlasnik, new VlasnikDTO()))
                .toList();
    }

    public VlasnikDTO get(final Integer vlasnikId) {
        return vlasnikRepository.findById(vlasnikId)
                .map(vlasnik -> mapToDTO(vlasnik, new VlasnikDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final VlasnikDTO vlasnikDTO) {
        final Vlasnik vlasnik = new Vlasnik();
        mapToEntity(vlasnikDTO, vlasnik);
        return vlasnikRepository.save(vlasnik).getVlasnikId();
    }

    public void update(final Integer vlasnikId, final VlasnikDTO vlasnikDTO) {
        final Vlasnik vlasnik = vlasnikRepository.findById(vlasnikId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(vlasnikDTO, vlasnik);
        vlasnikRepository.save(vlasnik);
    }

    public void delete(final Integer vlasnikId) {
        vlasnikRepository.deleteById(vlasnikId);
    }

    private VlasnikDTO mapToDTO(final Vlasnik vlasnik, final VlasnikDTO vlasnikDTO) {
        vlasnikDTO.setVlasnikId(vlasnik.getVlasnikId());
        vlasnikDTO.setVlasnikIme(vlasnik.getVlasnikIme());
        vlasnikDTO.setVlasnikPrezime(vlasnik.getVlasnikPrezime());
        vlasnikDTO.setVlasnikEmail(vlasnik.getVlasnikEmail());
        vlasnikDTO.setVlasnikSifra(vlasnik.getVlasnikSifra());
        vlasnikDTO.setTrgovina(vlasnik.getTrgovina() == null ? null : vlasnik.getTrgovina().getTrgovinaId());
        return vlasnikDTO;
    }

    private Vlasnik mapToEntity(final VlasnikDTO vlasnikDTO, final Vlasnik vlasnik) {
        vlasnik.setVlasnikIme(vlasnikDTO.getVlasnikIme());
        vlasnik.setVlasnikPrezime(vlasnikDTO.getVlasnikPrezime());
        vlasnik.setVlasnikEmail(vlasnikDTO.getVlasnikEmail());
        vlasnik.setVlasnikSifra(vlasnikDTO.getVlasnikSifra());
        final Trgovina trgovina = vlasnikDTO.getTrgovina() == null ? null : trgovinaRepository.findById(vlasnikDTO.getTrgovina())
                .orElseThrow(() -> new NotFoundException("trgovina not found"));
        vlasnik.setTrgovina(trgovina);
        return vlasnik;
    }

}
