package com.mojkvart.rest;

import com.mojkvart.model.OcjenaProizvodKupacDTO;
import com.mojkvart.service.OcjenaProizvodKupacService;
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
@RequestMapping(value = "/api/ocjenaProizvodKupacs", produces = MediaType.APPLICATION_JSON_VALUE)
public class OcjenaProizvodKupacResource {

    private final OcjenaProizvodKupacService ocjenaProizvodKupacService;

    public OcjenaProizvodKupacResource(
            final OcjenaProizvodKupacService ocjenaProizvodKupacService) {
        this.ocjenaProizvodKupacService = ocjenaProizvodKupacService;
    }

    @GetMapping
    public ResponseEntity<List<OcjenaProizvodKupacDTO>> getAllOcjenaProizvodKupacs() {
        return ResponseEntity.ok(ocjenaProizvodKupacService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OcjenaProizvodKupacDTO> getOcjenaProizvodKupac(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(ocjenaProizvodKupacService.get(id));
    }

    //UC21, koristite api/ocjenaProizvodKupacs za davanje ocjene proizvodu, saljete JSON 
    @PostMapping
    public ResponseEntity<Long> createOcjenaProizvodKupac(
            @RequestBody @Valid final OcjenaProizvodKupacDTO ocjenaProizvodKupacDTO) {
        final Long createdId = ocjenaProizvodKupacService.create(ocjenaProizvodKupacDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateOcjenaProizvodKupac(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final OcjenaProizvodKupacDTO ocjenaProizvodKupacDTO) {
        ocjenaProizvodKupacService.update(id, ocjenaProizvodKupacDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOcjenaProizvodKupac(
            @PathVariable(name = "id") final Long id) {
        ocjenaProizvodKupacService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
