package com.mojkvart.controllers;

import com.mojkvart.dtos.PopustDTO;
import com.mojkvart.service.PopustService;
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
@RequestMapping(value = "/api/popusts", produces = MediaType.APPLICATION_JSON_VALUE)
public class PopustResource {

    private final PopustService popustService;

    public PopustResource(final PopustService popustService) {
        this.popustService = popustService;
    }

    @GetMapping
    public ResponseEntity<List<PopustDTO>> getAllPopusts() {
        return ResponseEntity.ok(popustService.findAll());
    }

    @GetMapping("/{popustId}")
    public ResponseEntity<PopustDTO> getPopust(
            @PathVariable(name = "popustId") final Integer popustId) {
        return ResponseEntity.ok(popustService.get(popustId));
    }

    @PostMapping
    public ResponseEntity<Integer> createPopust(@RequestBody @Valid final PopustDTO popustDTO) {
        final Integer createdPopustId = popustService.create(popustDTO);
        return new ResponseEntity<>(createdPopustId, HttpStatus.CREATED);
    }

    @PutMapping("/{popustId}")
    public ResponseEntity<Integer> updatePopust(
            @PathVariable(name = "popustId") final Integer popustId,
            @RequestBody @Valid final PopustDTO popustDTO) {
        popustService.update(popustId, popustDTO);
        return ResponseEntity.ok(popustId);
    }

    @DeleteMapping("/{popustId}")
    public ResponseEntity<Void> deletePopust(
            @PathVariable(name = "popustId") final Integer popustId) {
        popustService.delete(popustId);
        return ResponseEntity.noContent().build();
    }

}
