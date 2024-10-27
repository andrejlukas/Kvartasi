package com.mojkvart.controllers;

import com.mojkvart.dtos.PonudaPopustDTO;
import com.mojkvart.service.PonudaPopustService;
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
@RequestMapping(value = "/api/ponudaPopusts", produces = MediaType.APPLICATION_JSON_VALUE)
public class PonudaPopustResource {

    private final PonudaPopustService ponudaPopustService;

    public PonudaPopustResource(final PonudaPopustService ponudaPopustService) {
        this.ponudaPopustService = ponudaPopustService;
    }

    @GetMapping
    public ResponseEntity<List<PonudaPopustDTO>> getAllPonudaPopusts() {
        return ResponseEntity.ok(ponudaPopustService.findAll());
    }

    @GetMapping("/{ponudaPopustId}")
    public ResponseEntity<PonudaPopustDTO> getPonudaPopust(
            @PathVariable(name = "ponudaPopustId") final Integer ponudaPopustId) {
        return ResponseEntity.ok(ponudaPopustService.get(ponudaPopustId));
    }

    @PostMapping
    public ResponseEntity<Integer> createPonudaPopust(
            @RequestBody @Valid final PonudaPopustDTO ponudaPopustDTO) {
        final Integer createdPonudaPopustId = ponudaPopustService.create(ponudaPopustDTO);
        return new ResponseEntity<>(createdPonudaPopustId, HttpStatus.CREATED);
    }

    @PutMapping("/{ponudaPopustId}")
    public ResponseEntity<Integer> updatePonudaPopust(
            @PathVariable(name = "ponudaPopustId") final Integer ponudaPopustId,
            @RequestBody @Valid final PonudaPopustDTO ponudaPopustDTO) {
        ponudaPopustService.update(ponudaPopustId, ponudaPopustDTO);
        return ResponseEntity.ok(ponudaPopustId);
    }

    @DeleteMapping("/{ponudaPopustId}")
    public ResponseEntity<Void> deletePonudaPopust(
            @PathVariable(name = "ponudaPopustId") final Integer ponudaPopustId) {
        final ReferencedWarning referencedWarning = ponudaPopustService.getReferencedWarning(ponudaPopustId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        ponudaPopustService.delete(ponudaPopustId);
        return ResponseEntity.noContent().build();
    }

}
