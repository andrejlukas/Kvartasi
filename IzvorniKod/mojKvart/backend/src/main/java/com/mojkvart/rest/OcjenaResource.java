package com.mojkvart.rest;

import com.mojkvart.model.OcjenaDTO;
import com.mojkvart.service.OcjenaService;
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
@RequestMapping(value = "/api/ocjenas", produces = MediaType.APPLICATION_JSON_VALUE)
public class OcjenaResource {

    private final OcjenaService ocjenaService;

    public OcjenaResource(final OcjenaService ocjenaService) {
        this.ocjenaService = ocjenaService;
    }

    @GetMapping
    public ResponseEntity<List<OcjenaDTO>> getAllOcjenas() {
        return ResponseEntity.ok(ocjenaService.findAll());
    }

    @GetMapping("/{ocjenaId}")
    public ResponseEntity<OcjenaDTO> getOcjena(
            @PathVariable(name = "ocjenaId") final Integer ocjenaId) {
        return ResponseEntity.ok(ocjenaService.get(ocjenaId));
    }

    @PostMapping
    public ResponseEntity<Integer> createOcjena(@RequestBody @Valid final OcjenaDTO ocjenaDTO) {
        final Integer createdOcjenaId = ocjenaService.create(ocjenaDTO);
        return new ResponseEntity<>(createdOcjenaId, HttpStatus.CREATED);
    }

    @PutMapping("/{ocjenaId}")
    public ResponseEntity<Integer> updateOcjena(
            @PathVariable(name = "ocjenaId") final Integer ocjenaId,
            @RequestBody @Valid final OcjenaDTO ocjenaDTO) {
        ocjenaService.update(ocjenaId, ocjenaDTO);
        return ResponseEntity.ok(ocjenaId);
    }

    @DeleteMapping("/{ocjenaId}")
    public ResponseEntity<Void> deleteOcjena(
            @PathVariable(name = "ocjenaId") final Integer ocjenaId) {
        final ReferencedWarning referencedWarning = ocjenaService.getReferencedWarning(ocjenaId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        ocjenaService.delete(ocjenaId);
        return ResponseEntity.noContent().build();
    }

}
