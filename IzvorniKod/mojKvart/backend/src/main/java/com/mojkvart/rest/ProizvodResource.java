package com.mojkvart.rest;

import com.mojkvart.model.ProizvodDTO;
import com.mojkvart.service.ProizvodService;
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
@RequestMapping(value = "/api/proizvods", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProizvodResource {

    private final ProizvodService proizvodService;

    public ProizvodResource(final ProizvodService proizvodService) {
        this.proizvodService = proizvodService;
    }

    @GetMapping
    public ResponseEntity<List<ProizvodDTO>> getAllProizvods() {
        return ResponseEntity.ok(proizvodService.findAll());
    }

    @GetMapping("/{proizvodId}")
    public ResponseEntity<ProizvodDTO> getProizvod(
            @PathVariable(name = "proizvodId") final Integer proizvodId) {
        return ResponseEntity.ok(proizvodService.get(proizvodId));
    }

    @PostMapping
    public ResponseEntity<Integer> createProizvod(
            @RequestBody @Valid final ProizvodDTO proizvodDTO) {
        final Integer createdProizvodId = proizvodService.create(proizvodDTO);
        return new ResponseEntity<>(createdProizvodId, HttpStatus.CREATED);
    }

    @PutMapping("/{proizvodId}")
    public ResponseEntity<Integer> updateProizvod(
            @PathVariable(name = "proizvodId") final Integer proizvodId,
            @RequestBody @Valid final ProizvodDTO proizvodDTO) {
        proizvodService.update(proizvodId, proizvodDTO);
        return ResponseEntity.ok(proizvodId);
    }

    @DeleteMapping("/{proizvodId}")
    public ResponseEntity<Void> deleteProizvod(
            @PathVariable(name = "proizvodId") final Integer proizvodId) {
        final ReferencedWarning referencedWarning = proizvodService.getReferencedWarning(proizvodId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        proizvodService.delete(proizvodId);
        return ResponseEntity.noContent().build();
    }

}
