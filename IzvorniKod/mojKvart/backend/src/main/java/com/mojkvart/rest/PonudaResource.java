package com.mojkvart.rest;

import com.mojkvart.model.PonudaDTO;
import com.mojkvart.service.DogadajService;
import com.mojkvart.service.PonudaService;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
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
@RequestMapping(value = "/api/ponudas", produces = MediaType.APPLICATION_JSON_VALUE)
public class PonudaResource {

    private final PonudaService ponudaService;

    public PonudaResource(final PonudaService ponudaService) {
        this.ponudaService = ponudaService;
    }

    // vraća sve ponude koje je moderator odobrio i koje kupac vec nije spremio ni iskoristio
    @GetMapping("/flag-true/{kupacId}")
    public ResponseEntity<List<PonudaDTO>> getPonudeForKupac( @PathVariable(name = "kupacId") Integer kupacId) {
        List<PonudaDTO> ponude = ponudaService.findAllWithFlagTrue(kupacId);
        return ResponseEntity.ok(ponude);
    }

    // vraća sve ponude koje moderator mora odobriti
    @GetMapping("/flag-false")
    public ResponseEntity<List<PonudaDTO>> getAllWithFlagFalse() {
        return ResponseEntity.ok(ponudaService.findAllWithFlagFalse());
    }

    // vraća sve ponude koje je moderator odobrio
    @GetMapping("/flag-true")
    public ResponseEntity<List<PonudaDTO>> getAllWithFlagTrue() {
        return ResponseEntity.ok(ponudaService.findAllWithFlagTrue());
    }

    @GetMapping("/{ponudaId}")
    public ResponseEntity<PonudaDTO> getPonuda(
            @PathVariable(name = "ponudaId") final Integer ponudaId) {
        return ResponseEntity.ok(ponudaService.get(ponudaId));
    }

    @GetMapping("/valid/{trgovinaId}")
    public ResponseEntity<List<PonudaDTO>> getTrgovinasValidPonudas(@PathVariable(name = "trgovinaId") final Integer trgovinaId) {
        return ResponseEntity.ok(ponudaService.findAllTrgovinaValidPonudas(trgovinaId));
    }

    @GetMapping("/invalid/{trgovinaId}")
    public ResponseEntity<List<PonudaDTO>> getTrgovinasNonValidPonudas(@PathVariable(name = "trgovinaId") final Integer trgovinaId) {
        return ResponseEntity.ok(ponudaService.findAllTrgovinaNonValidPonudas(trgovinaId));
    }

    @PostMapping("/check")
    public ResponseEntity<String> checkPonuda(@RequestBody @Valid final PonudaDTO ponudaDTO) {
        if(ponudaDTO.getPonudaNaziv().length() < 2)
            return ResponseEntity.badRequest().body("Naziv ponude treba biti minimalno duljine 2!");
        if(ponudaDTO.getPonudaOpis().length() < 2)
            return ResponseEntity.badRequest().body("Opis ponude treba biti minimalno duljine 2!");
        try {
            LocalDateTime rok = DogadajService.getVrijeme(ponudaDTO.getPonudaRok());
            if(rok.isBefore(LocalDateTime.now()))
                throw new RuntimeException("Datum roka mora biti u budućnosti!");
        } catch(Exception e) {
            if(e.getMessage().startsWith("Datum")) return ResponseEntity.badRequest().body(e.getMessage());
            return ResponseEntity.badRequest().body("Datum i vrijeme mora biti u formatu \"dd.MM.gggg. ss:mm\"!");
        }
        return ResponseEntity.ok("Sve ok!");
    }

    @PostMapping
    public ResponseEntity<Integer> createPonuda(@RequestBody @Valid final PonudaDTO ponudaDTO) {
        final Integer createdPonudaId = ponudaService.create(ponudaDTO);
        return new ResponseEntity<>(createdPonudaId, HttpStatus.CREATED);
    }

    // za promijenu zastavice iz false u true pogledajte komentar kod putMapping u ponudaPopust
    @PutMapping("/{ponudaId}")
    public ResponseEntity<Integer> updatePonuda(
            @PathVariable(name = "ponudaId") final Integer ponudaId,
            @RequestBody @Valid final PonudaDTO ponudaDTO) {
        ponudaService.update(ponudaId, ponudaDTO);
        return ResponseEntity.ok(ponudaId);
    }

    @DeleteMapping("/{ponudaId}")
    public ResponseEntity<Void> deletePonuda(
            @PathVariable(name = "ponudaId") final Integer ponudaId) {
        ponudaService.delete(ponudaId);
        return ResponseEntity.noContent().build();
    }
}