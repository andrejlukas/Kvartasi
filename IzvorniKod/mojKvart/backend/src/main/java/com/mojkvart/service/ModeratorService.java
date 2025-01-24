package com.mojkvart.service;

import com.mojkvart.domain.Moderator;
import com.mojkvart.model.AdministratorDTO;
import com.mojkvart.model.ModeratorDTO;
import com.mojkvart.repos.ModeratorRepository;
import com.mojkvart.util.NotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ModeratorService {

    private final ModeratorRepository moderatorRepository;

    public ModeratorService(final ModeratorRepository moderatorRepository) {
        this.moderatorRepository = moderatorRepository;
    }

    public List<ModeratorDTO> findAll() {
        final List<Moderator> moderators = moderatorRepository.findAll(Sort.by("moderatorId"));
        return moderators.stream()
                .map(moderator -> mapToDTO(moderator, new ModeratorDTO()))
                .toList();
    }

    public List<ModeratorDTO> findAllVerified() {
        final List<Moderator> moderators = moderatorRepository.findAll(Sort.by("moderatorId"));
        return moderators.stream()
                .filter(moderator -> moderator.getModeratorStatus().equals("V"))
                .map(moderator -> mapToDTO(moderator, new ModeratorDTO()))
                .toList();
    }

    public List<ModeratorDTO> findAllNotVerified() {
        final List<Moderator> moderators = moderatorRepository.findAll(Sort.by("moderatorId"));
        return moderators.stream()
                .filter(moderator -> moderator.getModeratorStatus().equals("N"))
                .map(moderator -> mapToDTO(moderator, new ModeratorDTO()))
                .toList();
    }

    public List<ModeratorDTO> findAllSuspended() {
        final List<Moderator> moderators = moderatorRepository.findAll(Sort.by("moderatorId"));
        return moderators.stream()
                .filter(moderator -> moderator.getModeratorStatus().equals("S"))
                .map(moderator -> mapToDTO(moderator, new ModeratorDTO()))
                .toList();
    }

    public Optional<ModeratorDTO> findByModeratorEmail(String email) {
        return moderatorRepository.findByModeratorEmail(email)
                .map(moderator -> mapToDTO(moderator, new ModeratorDTO()));
    }

    public ModeratorDTO get(final Integer moderatorId) {
        return moderatorRepository.findById(moderatorId)
                .map(moderator -> mapToDTO(moderator, new ModeratorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ModeratorDTO moderatorDTO) {
        final Moderator moderator = new Moderator();
        mapToEntity(moderatorDTO, moderator);
        return moderatorRepository.save(moderator).getModeratorId();
    }

    public void update(final Integer moderatorId, final ModeratorDTO moderatorDTO) {
        final Moderator moderator = moderatorRepository.findById(moderatorId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(moderatorDTO, moderator);
        moderatorRepository.save(moderator);
    }

    public void delete(final Integer moderatorId) {
        moderatorRepository.deleteById(moderatorId);
    }

    private ModeratorDTO mapToDTO(final Moderator moderator, final ModeratorDTO moderatorDTO) {
        moderatorDTO.setModeratorId(moderator.getModeratorId());
        moderatorDTO.setModeratorIme(moderator.getModeratorIme());
        moderatorDTO.setModeratorPrezime(moderator.getModeratorPrezime());
        moderatorDTO.setModeratorEmail(moderator.getModeratorEmail());
        moderatorDTO.setModeratorSifra(moderator.getModeratorSifra());
        moderatorDTO.setModeratorStatus(moderator.getModeratorStatus());
        return moderatorDTO;
    }

    private Moderator mapToEntity(final ModeratorDTO moderatorDTO, final Moderator moderator) {
        moderator.setModeratorIme(moderatorDTO.getModeratorIme());
        moderator.setModeratorPrezime(moderatorDTO.getModeratorPrezime());
        moderator.setModeratorEmail(moderatorDTO.getModeratorEmail());
        moderator.setModeratorSifra(moderatorDTO.getModeratorSifra());
        moderator.setModeratorStatus(moderatorDTO.getModeratorStatus());
        return moderator;
    }

}
