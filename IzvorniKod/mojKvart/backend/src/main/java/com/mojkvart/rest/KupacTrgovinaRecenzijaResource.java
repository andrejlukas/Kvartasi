package com.mojkvart.rest;

import com.mojkvart.domain.Kupac;
import com.mojkvart.domain.KupacTrgovinaRecenzija;
import com.mojkvart.model.KupacTrgovinaRecenzijaDTO;
import com.mojkvart.repos.KupacRepository;
import com.mojkvart.service.KupacService;
import com.mojkvart.service.KupacTrgovinaRecenzijaService;
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
@RequestMapping(value = "/api/kupacTrgovinaRecenzijas", produces = MediaType.APPLICATION_JSON_VALUE)
public class KupacTrgovinaRecenzijaResource {

    private final KupacTrgovinaRecenzijaService kupacTrgovinaRecenzijaService;

    public KupacTrgovinaRecenzijaResource(
            final KupacTrgovinaRecenzijaService kupacTrgovinaRecenzijaService) {
        this.kupacTrgovinaRecenzijaService = kupacTrgovinaRecenzijaService;
    }

    @GetMapping
    public ResponseEntity<List<KupacTrgovinaRecenzijaDTO>> getAllKupacTrgovinaRecenzijas() {
        return ResponseEntity.ok(kupacTrgovinaRecenzijaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KupacTrgovinaRecenzijaDTO> getKupacTrgovinaRecenzija(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(kupacTrgovinaRecenzijaService.get(id));
    }

    //UC20, koristite api/kupacTrgovinaRecenzijas za pisanje nove recenzije trgovini
    @PostMapping
    public ResponseEntity<Long> createKupacTrgovinaRecenzija(
            @RequestBody @Valid final KupacTrgovinaRecenzijaDTO kupacTrgovinaRecenzijaDTO) {
        final Long createdId = kupacTrgovinaRecenzijaService.create(kupacTrgovinaRecenzijaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateKupacTrgovinaRecenzija(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final KupacTrgovinaRecenzijaDTO kupacTrgovinaRecenzijaDTO) {
        kupacTrgovinaRecenzijaService.update(id, kupacTrgovinaRecenzijaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKupacTrgovinaRecenzija(
            @PathVariable(name = "id") final Long id) {
        kupacTrgovinaRecenzijaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
