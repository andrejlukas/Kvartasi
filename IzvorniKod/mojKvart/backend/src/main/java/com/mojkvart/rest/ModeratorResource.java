package com.mojkvart.rest;

import com.mojkvart.model.ModeratorDTO;
import com.mojkvart.service.ModeratorService;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/moderators", produces = MediaType.APPLICATION_JSON_VALUE)
public class ModeratorResource {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ModeratorService moderatorService;

    public ModeratorResource(final ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @GetMapping
    public ResponseEntity<List<ModeratorDTO>> getAllModerators() {
        return ResponseEntity.ok(moderatorService.findAll());
    }

    @GetMapping("/verified")
    public ResponseEntity<List<ModeratorDTO>> getAllVerifiedModerators() {
        return ResponseEntity.ok(moderatorService.findAllVerified());
    }

    @GetMapping("/notverified")
    public ResponseEntity<List<ModeratorDTO>> getAllNotVerifiedModerators() {
        return ResponseEntity.ok(moderatorService.findAllNotVerified());
    }

    @GetMapping("/suspended")
    public ResponseEntity<List<ModeratorDTO>> getAllSuspendedModerators() {
        return ResponseEntity.ok(moderatorService.findAllSuspended());
    }

    @GetMapping("/{moderatorEmail}")
    public ResponseEntity<ModeratorDTO> getModerator(
            @PathVariable(name = "moderatorEmail") final String moderatorEmail) {
        return ResponseEntity.ok(moderatorService.findByModeratorEmail(moderatorEmail).get());
    }

    //UC5, koristite api/moderators i pošaljite JSON objekt za kreiranje novog moderatora
    @PostMapping
    public ResponseEntity<Integer> createModerator(
            @RequestBody @Valid final ModeratorDTO moderatorDTO) {
        moderatorDTO.setModeratorSifra(passwordEncoder.encode(moderatorDTO.getModeratorSifra()));
        final Integer createdModeratorId = moderatorService.create(moderatorDTO);
        return new ResponseEntity<>(createdModeratorId, HttpStatus.CREATED);
    }

    @PutMapping("/{moderatorId}")
    public ResponseEntity<Integer> updateModerator(
            @PathVariable(name = "moderatorId") final Integer moderatorId,
            @RequestBody @Valid final ModeratorDTO moderatorDTO) {
        moderatorService.update(moderatorId, moderatorDTO);
        return ResponseEntity.ok(moderatorId);
    }

    //UC6, koristite api/moderators/{moderatorId} za brisanje moderatora iz sustava
    @DeleteMapping("/{moderatorId}")
    public ResponseEntity<Void> deleteModerator(
            @PathVariable(name = "moderatorId") final Integer moderatorId) {
        moderatorService.delete(moderatorId);
        return ResponseEntity.noContent().build();
    }

}
