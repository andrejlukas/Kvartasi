package com.mojkvart.controllers;

import com.mojkvart.dtos.ModeratorDTO;
import com.mojkvart.service.ModeratorService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    private final ModeratorService moderatorService;

    public ModeratorResource(final ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @GetMapping
    public ResponseEntity<List<ModeratorDTO>> getAllModerators() {
        return ResponseEntity.ok(moderatorService.findAll());
    }

    @GetMapping("/{moderatorId}")
    public ResponseEntity<ModeratorDTO> getModerator(
            @PathVariable(name = "moderatorId") final Integer moderatorId) {
        return ResponseEntity.ok(moderatorService.get(moderatorId));
    }

    @PostMapping
    public ResponseEntity<Integer> createModerator(
            @RequestBody @Valid final ModeratorDTO moderatorDTO) {
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

    @DeleteMapping("/{moderatorId}")
    public ResponseEntity<Void> deleteModerator(
            @PathVariable(name = "moderatorId") final Integer moderatorId) {
        moderatorService.delete(moderatorId);
        return ResponseEntity.noContent().build();
    }

}
