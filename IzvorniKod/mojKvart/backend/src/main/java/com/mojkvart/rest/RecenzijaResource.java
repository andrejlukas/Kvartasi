package com.mojkvart.rest;

import com.mojkvart.model.RecenzijaDTO;
import com.mojkvart.service.RecenzijaService;
import com.mojkvart.util.ReferencedException;
import com.mojkvart.util.ReferencedWarning;
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
@RequestMapping(value = "/api/recenzijas", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecenzijaResource {

    private final RecenzijaService recenzijaService;

    public RecenzijaResource(final RecenzijaService recenzijaService) {
        this.recenzijaService = recenzijaService;
    }

    @GetMapping
    public ResponseEntity<List<RecenzijaDTO>> getAllRecenzijas() {
        return ResponseEntity.ok(recenzijaService.findAll());
    }

    @GetMapping("/{recenzijaId}")
    public ResponseEntity<RecenzijaDTO> getRecenzija(
            @PathVariable(name = "recenzijaId") final Integer recenzijaId) {
        return ResponseEntity.ok(recenzijaService.get(recenzijaId));
    }

    @PostMapping
    public ResponseEntity<Integer> createRecenzija(
            @RequestBody @Valid final RecenzijaDTO recenzijaDTO) {
        final Integer createdRecenzijaId = recenzijaService.create(recenzijaDTO);
        return new ResponseEntity<>(createdRecenzijaId, HttpStatus.CREATED);
    }

    @PutMapping("/{recenzijaId}")
    public ResponseEntity<Integer> updateRecenzija(
            @PathVariable(name = "recenzijaId") final Integer recenzijaId,
            @RequestBody @Valid final RecenzijaDTO recenzijaDTO) {
        recenzijaService.update(recenzijaId, recenzijaDTO);
        return ResponseEntity.ok(recenzijaId);
    }

    @DeleteMapping("/{recenzijaId}")
    public ResponseEntity<Void> deleteRecenzija(
            @PathVariable(name = "recenzijaId") final Integer recenzijaId) {
        final ReferencedWarning referencedWarning = recenzijaService.getReferencedWarning(recenzijaId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        recenzijaService.delete(recenzijaId);
        return ResponseEntity.noContent().build();
    }

}
