package com.mojkvart.controllers;

import com.mojkvart.dtos.TrgovinaDTO;
import com.mojkvart.service.TrgovinaService;
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
@RequestMapping(value = "/api/trgovinas", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrgovinaResource {

    private final TrgovinaService trgovinaService;

    public TrgovinaResource(final TrgovinaService trgovinaService) {
        this.trgovinaService = trgovinaService;
    }

    @GetMapping
    public ResponseEntity<List<TrgovinaDTO>> getAllTrgovinas() {
        return ResponseEntity.ok(trgovinaService.findAll());
    }

    @GetMapping("/{trgovinaId}")
    public ResponseEntity<TrgovinaDTO> getTrgovina(
            @PathVariable(name = "trgovinaId") final Integer trgovinaId) {
        return ResponseEntity.ok(trgovinaService.get(trgovinaId));
    }

    @PostMapping
    public ResponseEntity<Integer> createTrgovina(
            @RequestBody @Valid final TrgovinaDTO trgovinaDTO) {
        final Integer createdTrgovinaId = trgovinaService.create(trgovinaDTO);
        return new ResponseEntity<>(createdTrgovinaId, HttpStatus.CREATED);
    }

    @PutMapping("/{trgovinaId}")
    public ResponseEntity<Integer> updateTrgovina(
            @PathVariable(name = "trgovinaId") final Integer trgovinaId,
            @RequestBody @Valid final TrgovinaDTO trgovinaDTO) {
        trgovinaService.update(trgovinaId, trgovinaDTO);
        return ResponseEntity.ok(trgovinaId);
    }

    @DeleteMapping("/{trgovinaId}")
    public ResponseEntity<Void> deleteTrgovina(
            @PathVariable(name = "trgovinaId") final Integer trgovinaId) {
        final ReferencedWarning referencedWarning = trgovinaService.getReferencedWarning(trgovinaId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        trgovinaService.delete(trgovinaId);
        return ResponseEntity.noContent().build();
    }

}