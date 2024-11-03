package com.mojkvart.rest;

import com.mojkvart.model.KupacDogadajTrgovinaDTO;
import com.mojkvart.service.KupacDogadajTrgovinaService;
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
@RequestMapping(value = "/api/kupacDogadajTrgovinas", produces = MediaType.APPLICATION_JSON_VALUE)
public class KupacDogadajTrgovinaResource {

    private final KupacDogadajTrgovinaService kupacDogadajTrgovinaService;

    public KupacDogadajTrgovinaResource(
            final KupacDogadajTrgovinaService kupacDogadajTrgovinaService) {
        this.kupacDogadajTrgovinaService = kupacDogadajTrgovinaService;
    }

    @GetMapping
    public ResponseEntity<List<KupacDogadajTrgovinaDTO>> getAllKupacDogadajTrgovinas() {
        return ResponseEntity.ok(kupacDogadajTrgovinaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KupacDogadajTrgovinaDTO> getKupacDogadajTrgovina(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(kupacDogadajTrgovinaService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createKupacDogadajTrgovina(
            @RequestBody @Valid final KupacDogadajTrgovinaDTO kupacDogadajTrgovinaDTO) {
        final Long createdId = kupacDogadajTrgovinaService.create(kupacDogadajTrgovinaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateKupacDogadajTrgovina(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final KupacDogadajTrgovinaDTO kupacDogadajTrgovinaDTO) {
        kupacDogadajTrgovinaService.update(id, kupacDogadajTrgovinaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKupacDogadajTrgovina(
            @PathVariable(name = "id") final Long id) {
        kupacDogadajTrgovinaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
