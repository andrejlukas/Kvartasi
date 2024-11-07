package com.mojkvart.rest;

import com.mojkvart.model.DogadajDTO;
import com.mojkvart.service.DogadajService;
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
@RequestMapping(value = "/api/dogadajs", produces = MediaType.APPLICATION_JSON_VALUE)
public class DogadajResource {

    private final DogadajService dogadajService;

    public DogadajResource(final DogadajService dogadajService) {
        this.dogadajService = dogadajService;
    }

    //UC10, koristite api/dogadajs za dohvacanje svih dogadaja od strane moderatora
    //UC17, koristite api/dogadajs za dohvacanje svih dogadaja od strane korisnika
    @GetMapping
    public ResponseEntity<List<DogadajDTO>> getAllDogadajs() {
        return ResponseEntity.ok(dogadajService.findAll());
    }

    @GetMapping("/{dogadajId}")
    public ResponseEntity<DogadajDTO> getDogadaj(
            @PathVariable(name = "dogadajId") final Integer dogadajId) {
        return ResponseEntity.ok(dogadajService.get(dogadajId));
    }

    //UC12, koristite api/dogadajs i posaljite JSON objekt za kreiranje novog dogadaja od strane trgovine
    @PostMapping
    public ResponseEntity<Integer> createDogadaj(@RequestBody @Valid final DogadajDTO dogadajDTO) {
        final Integer createdDogadajId = dogadajService.create(dogadajDTO);
        return new ResponseEntity<>(createdDogadajId, HttpStatus.CREATED);
    }

    @PutMapping("/{dogadajId}")
    public ResponseEntity<Integer> updateDogadaj(
            @PathVariable(name = "dogadajId") final Integer dogadajId,
            @RequestBody @Valid final DogadajDTO dogadajDTO) {
        dogadajService.update(dogadajId, dogadajDTO);
        return ResponseEntity.ok(dogadajId);
    }

    //UC10, koristite api/dogadajs/{dogadajId} za brisanje nekog dogadaja od strane moderatora
    @DeleteMapping("/{dogadajId}")
    public ResponseEntity<Void> deleteDogadaj(
            @PathVariable(name = "dogadajId") final Integer dogadajId) {
        final ReferencedWarning referencedWarning = dogadajService.getReferencedWarning(dogadajId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        dogadajService.delete(dogadajId);
        return ResponseEntity.noContent().build();
    }

}
