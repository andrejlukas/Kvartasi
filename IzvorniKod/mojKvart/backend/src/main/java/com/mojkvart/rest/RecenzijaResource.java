package com.mojkvart.rest;

import com.mojkvart.model.RecenzijaDTO;
import com.mojkvart.service.RecenzijaService;
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
@RequestMapping(value = "/api/recenzijas", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecenzijaResource {

    private final RecenzijaService recenzijaService;

    public RecenzijaResource(final RecenzijaService recenzijaService) {
        this.recenzijaService = recenzijaService;
    }

    @GetMapping
    public ResponseEntity<List<RecenzijaDTO>> getAllRecenzijas() {
        return ResponseEntity.ok(recenzijaService.findAll());
    }

    // API za dohvaćanje svih recenzija koje je moderator potvrdio
    @GetMapping("/approved")
    public ResponseEntity<List<RecenzijaDTO>> getAllApproved() {
        return ResponseEntity.ok(recenzijaService.getAllApproved());
    }

    // API za dohvaćanje svih recenzija koje moderator mora potvrditi
    @GetMapping("/not-approved")
    public ResponseEntity<List<RecenzijaDTO>> getAllNotApproved() {
        return ResponseEntity.ok(recenzijaService.getAllNotApproved());
    }

    @GetMapping("/{recenzijaId}")
    public ResponseEntity<RecenzijaDTO> getRecenzija(
            @PathVariable(name = "recenzijaId") final Integer recenzijaId) {
        return ResponseEntity.ok(recenzijaService.get(recenzijaId));
    }

    @PostMapping
    public ResponseEntity<Integer> createRecenzija(
            @RequestBody @Valid final RecenzijaDTO recenzijaDTO) {
        final Integer createdRecenzijaId = recenzijaService.create(recenzijaDTO);
        return new ResponseEntity<>(createdRecenzijaId, HttpStatus.CREATED);
    }

    //UC14, koristite api/recenzijas/{recenzijaId} za upisivanje odgovora u recenzijaOdgovor atribut
    @PutMapping("/{recenzijaId}")
    public ResponseEntity<Integer> updateRecenzija(
            @PathVariable(name = "recenzijaId") final Integer recenzijaId,
            @RequestBody @Valid final RecenzijaDTO recenzijaDTO) {
        recenzijaService.update(recenzijaId, recenzijaDTO);
        return ResponseEntity.ok(recenzijaId);
    }

    // koristite za lako dodavanje odgovora na receziju
    @PutMapping("/odgovor/{recenzijaId}")
    public ResponseEntity<Void> dodajOdgovorNaRecenziju(@PathVariable(name = "recenzijaId") Integer recenzijaId,
                                                        @RequestBody String odgovor) {
        recenzijaService.dodajOdgovorNaRecenziju(recenzijaId, odgovor);
        return ResponseEntity.ok().build();
    }

    // koristite kad je recenziju odobrio moderator za promjenu zastavice
    @PutMapping("/odobrioModerator/{recenzijaId}")
    public ResponseEntity<Void> promijeniZastavicu(@PathVariable(name = "recenzijaId") Integer recenzijaId) {
        recenzijaService.promijeniZastavicu(recenzijaId);
        return ResponseEntity.ok().build();
    }

    //UC9, koristite api/recenzijas/{recenzijaId} za brisanje recenzija
    @DeleteMapping("/{recenzijaId}")
    public ResponseEntity<Void> deleteRecenzija(
            @PathVariable(name = "recenzijaId") final Integer recenzijaId) {
        final ReferencedWarning referencedWarning = recenzijaService.getReferencedWarning(recenzijaId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        recenzijaService.delete(recenzijaId);
        return ResponseEntity.noContent().build();
    }

    //UC14, Kada zelite dobiti sve recenzije od neke trgovine saljete zahtjev na http://localhost:8080/api/recenzijas/trgovinas/{idtrgovine}

    @GetMapping("/trgovinas/{trgovinaId}")
public ResponseEntity<List<RecenzijaDTO>> getTrgovinasRecenzijas(@PathVariable(name = "trgovinaId") final Integer id) {
    return ResponseEntity.ok(recenzijaService.getTrgovinasRecenzijas(id));
}

//kada zelite dobiti sve recenzije nekog korisnika koristite ovu rutu

@GetMapping("/kupacs/{kupacId}")
public ResponseEntity<List<RecenzijaDTO>> getKupacRecenzijas(@PathVariable(name = "kupacId") final Integer id) {
    return ResponseEntity.ok(recenzijaService.getKupacsRecenzijas(id));
}


}
