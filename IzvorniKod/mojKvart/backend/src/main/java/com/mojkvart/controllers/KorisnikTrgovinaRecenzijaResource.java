package com.mojkvart.controllers;

import com.mojkvart.dtos.KorisnikTrgovinaRecenzijaDTO;
import com.mojkvart.service.KorisnikTrgovinaRecenzijaService;
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
@RequestMapping(value = "/api/korisnikTrgovinaRecenzijas", produces = MediaType.APPLICATION_JSON_VALUE)
public class KorisnikTrgovinaRecenzijaResource {

    private final KorisnikTrgovinaRecenzijaService korisnikTrgovinaRecenzijaService;

    public KorisnikTrgovinaRecenzijaResource(
            final KorisnikTrgovinaRecenzijaService korisnikTrgovinaRecenzijaService) {
        this.korisnikTrgovinaRecenzijaService = korisnikTrgovinaRecenzijaService;
    }

    @GetMapping
    public ResponseEntity<List<KorisnikTrgovinaRecenzijaDTO>> getAllKorisnikTrgovinaRecenzijas() {
        return ResponseEntity.ok(korisnikTrgovinaRecenzijaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KorisnikTrgovinaRecenzijaDTO> getKorisnikTrgovinaRecenzija(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(korisnikTrgovinaRecenzijaService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createKorisnikTrgovinaRecenzija(
            @RequestBody @Valid final KorisnikTrgovinaRecenzijaDTO korisnikTrgovinaRecenzijaDTO) {
        final Long createdId = korisnikTrgovinaRecenzijaService.create(korisnikTrgovinaRecenzijaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateKorisnikTrgovinaRecenzija(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final KorisnikTrgovinaRecenzijaDTO korisnikTrgovinaRecenzijaDTO) {
        korisnikTrgovinaRecenzijaService.update(id, korisnikTrgovinaRecenzijaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKorisnikTrgovinaRecenzija(
            @PathVariable(name = "id") final Long id) {
        korisnikTrgovinaRecenzijaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
