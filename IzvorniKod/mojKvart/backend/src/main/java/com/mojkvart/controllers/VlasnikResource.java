package com.mojkvart.controllers;

import com.mojkvart.dtos.VlasnikDTO;
import com.mojkvart.service.VlasnikService;
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
@RequestMapping(value = "/api/vlasniks", produces = MediaType.APPLICATION_JSON_VALUE)
public class VlasnikResource {

    private final VlasnikService vlasnikService;

    public VlasnikResource(final VlasnikService vlasnikService) {
        this.vlasnikService = vlasnikService;
    }

    @GetMapping
    public ResponseEntity<List<VlasnikDTO>> getAllVlasniks() {
        return ResponseEntity.ok(vlasnikService.findAll());
    }

    @GetMapping("/{vlasnikId}")
    public ResponseEntity<VlasnikDTO> getVlasnik(
            @PathVariable(name = "vlasnikId") final Integer vlasnikId) {
        return ResponseEntity.ok(vlasnikService.get(vlasnikId));
    }

    @PostMapping
    public ResponseEntity<Integer> createVlasnik(@RequestBody @Valid final VlasnikDTO vlasnikDTO) {
        final Integer createdVlasnikId = vlasnikService.create(vlasnikDTO);
        return new ResponseEntity<>(createdVlasnikId, HttpStatus.CREATED);
    }

    @PutMapping("/{vlasnikId}")
    public ResponseEntity<Integer> updateVlasnik(
            @PathVariable(name = "vlasnikId") final Integer vlasnikId,
            @RequestBody @Valid final VlasnikDTO vlasnikDTO) {
        vlasnikService.update(vlasnikId, vlasnikDTO);
        return ResponseEntity.ok(vlasnikId);
    }

    @DeleteMapping("/{vlasnikId}")
    public ResponseEntity<Void> deleteVlasnik(
            @PathVariable(name = "vlasnikId") final Integer vlasnikId) {
        vlasnikService.delete(vlasnikId);
        return ResponseEntity.noContent().build();
    }

}
