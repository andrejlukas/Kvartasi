package com.mojkvart.rest;

import com.mojkvart.model.KupacDTO;
import com.mojkvart.service.KupacService;
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
@RequestMapping(value = "/api/kupacs", produces = MediaType.APPLICATION_JSON_VALUE)
public class KupacResource {

    private final KupacService kupacService;

    public KupacResource(final KupacService kupacService) {
        this.kupacService = kupacService;
    }
 
   
    @GetMapping
    public ResponseEntity<List<KupacDTO>> getAllKupacs() {
        return ResponseEntity.ok(kupacService.findAll());
    }

    //UC3, koristite api/kupacs/{kupacId} za dohvaćanje osobnih podataka
    @GetMapping("/{kupacId}")
    public ResponseEntity<KupacDTO> getKupac(
            @PathVariable(name = "kupacId") final Integer kupacId) {
        return ResponseEntity.ok(kupacService.get(kupacId));
    }

    //UC1, koristite api/kupacs i pošaljite JSON objekt za registraciju
    @PostMapping
    public ResponseEntity<Integer> createKupac(@RequestBody @Valid final KupacDTO kupacDTO) {
        final Integer createdKupacId = kupacService.create(kupacDTO);
        return new ResponseEntity<>(createdKupacId, HttpStatus.CREATED);
    }

    //UC3, koristite api/kupacs/{kupacId} za uređivanje osobnih podataka
    @PutMapping("/{kupacId}")
    public ResponseEntity<Integer> updateKupac(
            @PathVariable(name = "kupacId") final Integer kupacId,
            @RequestBody @Valid final KupacDTO kupacDTO) {
        kupacService.update(kupacId, kupacDTO);
        return ResponseEntity.ok(kupacId);
    }

    //UC6, koristite api/kupacs/{kupacId} za brisanje kupca iz sustava
    @DeleteMapping("/{kupacId}")
    public ResponseEntity<Void> deleteKupac(@PathVariable(name = "kupacId") final Integer kupacId) {
        final ReferencedWarning referencedWarning = kupacService.getReferencedWarning(kupacId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        kupacService.delete(kupacId);
        return ResponseEntity.noContent().build();
    }

}
