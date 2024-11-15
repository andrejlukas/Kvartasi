package com.mojkvart.service;

import com.mojkvart.domain.Administrator;
import com.mojkvart.model.AdministratorDTO;
import com.mojkvart.repos.AdministratorRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    public AdministratorService(final AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    public List<AdministratorDTO> findAll() {
        final List<Administrator> administrators = administratorRepository.findAll(Sort.by("administratorId"));
        return administrators.stream()
                .map(administrator -> mapToDTO(administrator, new AdministratorDTO()))
                .toList();
    }

    public Optional<AdministratorDTO> findByAdministratorEmail(String email) {
        return administratorRepository.findByAdministratorEmail(email)
                .map(administrator -> mapToDTO(administrator, new AdministratorDTO()));
    }

    public AdministratorDTO get(final Integer administratorId) {
        return administratorRepository.findById(administratorId)
                .map(administrator -> mapToDTO(administrator, new AdministratorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AdministratorDTO administratorDTO) {
        final Administrator administrator = new Administrator();
        mapToEntity(administratorDTO, administrator);
        return administratorRepository.save(administrator).getAdministratorId();
    }

    public void update(final Integer administratorId, final AdministratorDTO administratorDTO) {
        final Administrator administrator = administratorRepository.findById(administratorId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(administratorDTO, administrator);
        administratorRepository.save(administrator);
    }

    public void delete(final Integer administratorId) {
        administratorRepository.deleteById(administratorId);
    }

    private AdministratorDTO mapToDTO(final Administrator administrator,
            final AdministratorDTO administratorDTO) {
        administratorDTO.setAdministratorId(administrator.getAdministratorId());
        administratorDTO.setAdministratorIme(administrator.getAdministratorIme());
        administratorDTO.setAdministratorPrezime(administrator.getAdministratorPrezime());
        administratorDTO.setAdministratorEmail(administrator.getAdministratorEmail());
        administratorDTO.setAdministratorSifra(administrator.getAdministratorSifra());
        return administratorDTO;
    }

    private Administrator mapToEntity(final AdministratorDTO administratorDTO,
            final Administrator administrator) {
        administrator.setAdministratorIme(administratorDTO.getAdministratorIme());
        administrator.setAdministratorPrezime(administratorDTO.getAdministratorPrezime());
        administrator.setAdministratorEmail(administratorDTO.getAdministratorEmail());
        administrator.setAdministratorSifra(administratorDTO.getAdministratorSifra());
        return administrator;
    }

}


