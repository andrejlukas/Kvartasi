package com.mojkvart.rest;

import com.mojkvart.model.KupacTrgovinaPonudaPopustDTO;
import com.mojkvart.service.KupacTrgovinaPonudaPopustService;
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
@RequestMapping(value = "/api/kupacTrgovinaPonudaPopusts", produces = MediaType.APPLICATION_JSON_VALUE)
public class KupacTrgovinaPonudaPopustResource {

    private final KupacTrgovinaPonudaPopustService kupacTrgovinaPonudaPopustService;

    public KupacTrgovinaPonudaPopustResource(
            final KupacTrgovinaPonudaPopustService kupacTrgovinaPonudaPopustService) {
        this.kupacTrgovinaPonudaPopustService = kupacTrgovinaPonudaPopustService;
    }

    @GetMapping
    public ResponseEntity<List<KupacTrgovinaPonudaPopustDTO>> getAllKupacTrgovinaPonudaPopusts() {
        return ResponseEntity.ok(kupacTrgovinaPonudaPopustService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KupacTrgovinaPonudaPopustDTO> getKupacTrgovinaPonudaPopust(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(kupacTrgovinaPonudaPopustService.get(id));
    }

    //UC19, koristite api/kupacTrgovinaPonudaPopusts za spremanje ponude i popusta
    @PostMapping
    public ResponseEntity<Long> createKupacTrgovinaPonudaPopust(
            @RequestBody @Valid final KupacTrgovinaPonudaPopustDTO kupacTrgovinaPonudaPopustDTO) {
        final Long createdId = kupacTrgovinaPonudaPopustService.create(kupacTrgovinaPonudaPopustDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateKupacTrgovinaPonudaPopust(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final KupacTrgovinaPonudaPopustDTO kupacTrgovinaPonudaPopustDTO) {
        kupacTrgovinaPonudaPopustService.update(id, kupacTrgovinaPonudaPopustDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKupacTrgovinaPonudaPopust(
            @PathVariable(name = "id") final Long id) {
        kupacTrgovinaPonudaPopustService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
