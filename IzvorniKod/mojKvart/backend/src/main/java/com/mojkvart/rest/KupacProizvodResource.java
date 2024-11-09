package com.mojkvart.rest;

import com.mojkvart.model.KupacProizvodDTO;
import com.mojkvart.service.KupacProizvodService;
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
@RequestMapping(value = "/api/kupacProizvods", produces = MediaType.APPLICATION_JSON_VALUE)
public class KupacProizvodResource {

    private final KupacProizvodService kupacProizvodService;

    public KupacProizvodResource(
            final KupacProizvodService kupacProizvodService) {
        this.kupacProizvodService = kupacProizvodService;
    }

    @GetMapping
    public ResponseEntity<List<KupacProizvodDTO>> getAllkupacProizvods() {
        return ResponseEntity.ok(kupacProizvodService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KupacProizvodDTO> getkupacProizvod(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(kupacProizvodService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createkupacProizvod(
            @RequestBody @Valid final KupacProizvodDTO kupacProizvodDTO) {
        final Long createdId = kupacProizvodService.create(kupacProizvodDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatekupacProizvod(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final KupacProizvodDTO kupacProizvodDTO) {
        kupacProizvodService.update(id, kupacProizvodDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletekupacProizvod(
            @PathVariable(name = "id") final Long id) {
        kupacProizvodService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
