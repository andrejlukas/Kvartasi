package com.mojkvart.controllers;

import com.mojkvart.dtos.OcjenaProizvodKorisnikDTO;
import com.mojkvart.service.OcjenaProizvodKorisnikService;
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
@RequestMapping(value = "/api/ocjenaProizvodKorisniks", produces = MediaType.APPLICATION_JSON_VALUE)
public class OcjenaProizvodKorisnikResource {

    private final OcjenaProizvodKorisnikService ocjenaProizvodKorisnikService;

    public OcjenaProizvodKorisnikResource(
            final OcjenaProizvodKorisnikService ocjenaProizvodKorisnikService) {
        this.ocjenaProizvodKorisnikService = ocjenaProizvodKorisnikService;
    }

    @GetMapping
    public ResponseEntity<List<OcjenaProizvodKorisnikDTO>> getAllOcjenaProizvodKorisniks() {
        return ResponseEntity.ok(ocjenaProizvodKorisnikService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OcjenaProizvodKorisnikDTO> getOcjenaProizvodKorisnik(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(ocjenaProizvodKorisnikService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createOcjenaProizvodKorisnik(
            @RequestBody @Valid final OcjenaProizvodKorisnikDTO ocjenaProizvodKorisnikDTO) {
        final Long createdId = ocjenaProizvodKorisnikService.create(ocjenaProizvodKorisnikDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateOcjenaProizvodKorisnik(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final OcjenaProizvodKorisnikDTO ocjenaProizvodKorisnikDTO) {
        ocjenaProizvodKorisnikService.update(id, ocjenaProizvodKorisnikDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOcjenaProizvodKorisnik(
            @PathVariable(name = "id") final Long id) {
        ocjenaProizvodKorisnikService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
