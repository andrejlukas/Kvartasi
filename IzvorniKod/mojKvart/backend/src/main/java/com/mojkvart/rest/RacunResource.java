package com.mojkvart.rest;

import com.mojkvart.model.RacunDTO;
import com.mojkvart.service.RacunService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/racuns", produces = MediaType.APPLICATION_JSON_VALUE)
public class RacunResource {

    private final RacunService racunService;

    public RacunResource(final RacunService racunService) {
        this.racunService = racunService;
    }

    @GetMapping
    public ResponseEntity<List<RacunDTO>> getAllRacuns() {
        return ResponseEntity.ok(racunService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RacunDTO> getRacun(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(racunService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createracun(@RequestBody @Valid final RacunDTO racunDTO) {
        final Long createdId = racunService.create(racunDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRacun(@PathVariable(name = "id") final Long id, @RequestBody @Valid final RacunDTO racunDTO) {
        racunService.update(id, racunDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRacun(@PathVariable(name = "id") final Long id) {
        racunService.delete(id);
        return ResponseEntity.noContent().build();
    }
}