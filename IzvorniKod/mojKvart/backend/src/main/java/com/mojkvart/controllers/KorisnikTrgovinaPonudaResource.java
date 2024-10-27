package com.mojkvart.controllers;

import com.mojkvart.dtos.KorisnikTrgovinaPonudaDTO;
import com.mojkvart.service.KorisnikTrgovinaPonudaService;
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
@RequestMapping(value = "/api/korisnikTrgovinaPonudas", produces = MediaType.APPLICATION_JSON_VALUE)
public class KorisnikTrgovinaPonudaResource {

    private final KorisnikTrgovinaPonudaService korisnikTrgovinaPonudaService;

    public KorisnikTrgovinaPonudaResource(
            final KorisnikTrgovinaPonudaService korisnikTrgovinaPonudaService) {
        this.korisnikTrgovinaPonudaService = korisnikTrgovinaPonudaService;
    }

    @GetMapping
    public ResponseEntity<List<KorisnikTrgovinaPonudaDTO>> getAllKorisnikTrgovinaPonudas() {
        return ResponseEntity.ok(korisnikTrgovinaPonudaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KorisnikTrgovinaPonudaDTO> getKorisnikTrgovinaPonuda(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(korisnikTrgovinaPonudaService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createKorisnikTrgovinaPonuda(
            @RequestBody @Valid final KorisnikTrgovinaPonudaDTO korisnikTrgovinaPonudaDTO) {
        final Long createdId = korisnikTrgovinaPonudaService.create(korisnikTrgovinaPonudaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateKorisnikTrgovinaPonuda(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final KorisnikTrgovinaPonudaDTO korisnikTrgovinaPonudaDTO) {
        korisnikTrgovinaPonudaService.update(id, korisnikTrgovinaPonudaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKorisnikTrgovinaPonuda(
            @PathVariable(name = "id") final Long id) {
        korisnikTrgovinaPonudaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
