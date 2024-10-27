package com.mojkvart.controllers;

import com.mojkvart.dtos.KorisnikDogadajTrgovinaDTO;
import com.mojkvart.service.KorisnikDogadajTrgovinaService;
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
@RequestMapping(value = "/api/korisnikDogadajTrgovinas", produces = MediaType.APPLICATION_JSON_VALUE)
public class KorisnikDogadajTrgovinaResource {

    private final KorisnikDogadajTrgovinaService korisnikDogadajTrgovinaService;

    public KorisnikDogadajTrgovinaResource(
            final KorisnikDogadajTrgovinaService korisnikDogadajTrgovinaService) {
        this.korisnikDogadajTrgovinaService = korisnikDogadajTrgovinaService;
    }

    @GetMapping
    public ResponseEntity<List<KorisnikDogadajTrgovinaDTO>> getAllKorisnikDogadajTrgovinas() {
        return ResponseEntity.ok(korisnikDogadajTrgovinaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KorisnikDogadajTrgovinaDTO> getKorisnikDogadajTrgovina(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(korisnikDogadajTrgovinaService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createKorisnikDogadajTrgovina(
            @RequestBody @Valid final KorisnikDogadajTrgovinaDTO korisnikDogadajTrgovinaDTO) {
        final Long createdId = korisnikDogadajTrgovinaService.create(korisnikDogadajTrgovinaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateKorisnikDogadajTrgovina(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final KorisnikDogadajTrgovinaDTO korisnikDogadajTrgovinaDTO) {
        korisnikDogadajTrgovinaService.update(id, korisnikDogadajTrgovinaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKorisnikDogadajTrgovina(
            @PathVariable(name = "id") final Long id) {
        korisnikDogadajTrgovinaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
