package com.mojkvart.rest;

import com.mojkvart.model.KupacDogadajDTO;
import com.mojkvart.service.KupacDogadajService;
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
@RequestMapping(value = "/api/kupacDogadajs", produces = MediaType.APPLICATION_JSON_VALUE)
public class KupacDogadajResource {

    private final KupacDogadajService kupacDogadajService;

    public KupacDogadajResource(
            final KupacDogadajService kupacDogadajService) {
        this.kupacDogadajService = kupacDogadajService;
    }

    @GetMapping
    public ResponseEntity<List<com.mojkvart.domain.KupacDogadaj>> getAllkupacDogadajs() {
        return ResponseEntity.ok(kupacDogadajService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.mojkvart.domain.KupacDogadaj> getkupacDogadaj(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(kupacDogadajService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createkupacDogadaj(
            @RequestBody @Valid final KupacDogadajDTO kupacDogadajDTO) {
        final Long createdId = kupacDogadajService.create(kupacDogadajDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatekupacDogadaj(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final KupacDogadajDTO kupacDogadajDTO) {
        kupacDogadajService.update(id, kupacDogadajDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletekupacDogadaj(
            @PathVariable(name = "id") final Long id) {
        kupacDogadajService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
