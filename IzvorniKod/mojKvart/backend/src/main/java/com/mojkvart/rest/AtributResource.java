package com.mojkvart.rest;

import com.mojkvart.model.AtributDTO;
import com.mojkvart.service.AtributService;
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
@RequestMapping(value = "/api/atributs", produces = MediaType.APPLICATION_JSON_VALUE)
public class AtributResource {

    private final AtributService atributService;

    public AtributResource(final AtributService atributService) {
        this.atributService = atributService;
    }

    @GetMapping
    public ResponseEntity<List<AtributDTO>> getAllAtributs() {
        return ResponseEntity.ok(atributService.findAll());
    }

    @GetMapping("/{atributId}")
    public ResponseEntity<AtributDTO> getAtribut(
            @PathVariable(name = "atributId") final Integer atributId) {
        return ResponseEntity.ok(atributService.get(atributId));
    }

    @PostMapping
    public ResponseEntity<Integer> createAtribut(@RequestBody @Valid final AtributDTO atributDTO) {
        final Integer createdAtributId = atributService.create(atributDTO);
        return new ResponseEntity<>(createdAtributId, HttpStatus.CREATED);
    }

    @PutMapping("/{atributId}")
    public ResponseEntity<Integer> updateAtribut(
            @PathVariable(name = "atributId") final Integer atributId,
            @RequestBody @Valid final AtributDTO atributDTO) {
        atributService.update(atributId, atributDTO);
        return ResponseEntity.ok(atributId);
    }

    @DeleteMapping("/{atributId}")
    public ResponseEntity<Void> deleteAtribut(
            @PathVariable(name = "atributId") final Integer atributId) {
        atributService.delete(atributId);
        return ResponseEntity.noContent().build();
    }

}
