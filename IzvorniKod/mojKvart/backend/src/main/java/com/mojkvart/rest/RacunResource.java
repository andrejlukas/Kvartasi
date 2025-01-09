package com.mojkvart.rest;

import com.mojkvart.model.RacunDTO;
import com.mojkvart.service.RacunService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/racuns", produces = MediaType.APPLICATION_JSON_VALUE)
public class RacunResource {

    private final RacunService racunService;

    public RacunResource(final RacunService racunService) {
        this.racunService = racunService;
    }

    @GetMapping
    public ResponseEntity<List<RacunDTO>> getAllRacuns() {
        return ResponseEntity.ok(racunService.findAll());
    }

    // odlicna funkcija, sluzi da se provjeri postoji li vec racun za 
    // odredenog kupca i trgovinu, ako postoji vraca se Id racuna, ako ne postoji
    // stvara se novi racun i vraca se Id novog ravuna, s tim Idjem kreirate dalje KupacProizvod objekt

    @GetMapping("/{kupacId}/{trgovinaId}")
    public ResponseEntity<Long> getRacunForKupacAndTrgovina(@PathVariable Integer kupacId, @PathVariable Integer trgovinaId) {
        // Pozovi servisnu metodu za provjeru i kreiranje računa
        Long racunId = racunService.getRacunForKupacAndTrgovina(kupacId, trgovinaId);

        // Vratite računId u odgovoru
        return ResponseEntity.ok(racunId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RacunDTO> getRacun(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(racunService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createracun(@RequestBody @Valid final RacunDTO racunDTO) {
        final Long createdId = racunService.create(racunDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRacun(@PathVariable(name = "id") final Long id, @RequestBody @Valid final RacunDTO racunDTO) {
        racunService.update(id, racunDTO);
        return ResponseEntity.ok(id);
    }

    // funkcija koja sluzi za promjenu stanja racuna, ima tri moguca stanja
    // "K" - kosarica, "T" - trgovina i "P" - placena narudzba, ovo koristite kada
    // kupac klikne posalji ili kada trgovina oznaci da je kupac skupio narudzbu i platio
    
    @PutMapping("/stanje/{racunId}")
    public ResponseEntity<Void> promijeniStanje(
            @PathVariable(name = "racunId") final Long racunId,
            @RequestBody Map<String, Character> requestBody) {
            Character novoStanje = requestBody.get("novoStanje");
        racunService.promijeniStanje(racunId, novoStanje);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRacun(@PathVariable(name = "id") final Long id) {
        racunService.delete(id);
        return ResponseEntity.noContent().build();
    }
}