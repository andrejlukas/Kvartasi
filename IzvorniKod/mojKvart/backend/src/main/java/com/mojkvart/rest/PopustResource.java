package com.mojkvart.rest;

import com.mojkvart.model.PonudaPopustDTO;
import com.mojkvart.model.PopustDTO;
import com.mojkvart.service.DogadajService;
import com.mojkvart.service.PopustService;
import com.mojkvart.service.ProizvodService;
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
@RequestMapping(value = "/api/popusts", produces = MediaType.APPLICATION_JSON_VALUE)
public class PopustResource {

    private final PopustService popustService;

    public PopustResource(final PopustService popustService) {
        this.popustService = popustService;
    }
    
    // vraća sve popuste koje je moderator odobrio i koje kupac vec nije spremio ni iskoristio
    @GetMapping("/flag-true/{kupacId}")
    public ResponseEntity<List<PopustDTO>> getPopustiForKupac( @PathVariable(name = "kupacId") Integer kupacId) {
        List<PopustDTO> popusti = popustService.findAllWithFlagTrue(kupacId);
        return ResponseEntity.ok(popusti);
    }

    // vraca sve popuste koje moderator treba odobriti
    @GetMapping("/flag-false")
    public ResponseEntity<List<PopustDTO>> getAllPopustsWithFlagFalse() {
        return ResponseEntity.ok(popustService.findAllWithFlagFalse());
    }

    @GetMapping("/{popustId}")
    public ResponseEntity<PopustDTO> getPopust(
            @PathVariable(name = "popustId") final Integer popustId) {
        return ResponseEntity.ok(popustService.get(popustId));
    }

    @GetMapping("/valid/{trgovinaId}")
    public ResponseEntity<List<PopustDTO>> getTrgovinasValidPopusts(@PathVariable(name = "trgovinaId") final Integer trgovinaId) {
        return ResponseEntity.ok(popustService.findAllTrgovinaValidPopusts(trgovinaId));
    }

    @GetMapping("/invalid/{trgovinaId}")
    public ResponseEntity<List<PopustDTO>> getTrgovinasNonValidPopusts(@PathVariable(name = "trgovinaId") final Integer trgovinaId) {
        return ResponseEntity.ok(popustService.findAllTrgovinaNonValidPopusts(trgovinaId));
    }

    @PostMapping("/check")
    public ResponseEntity<String> checkPopust(
            @RequestBody @Valid final PopustDTO popustDTO) {
        if(popustDTO.getPopustNaziv().length() < 2)
            return ResponseEntity.badRequest().body("Naziv popusta treba biti minimalno duljine 2!");
        if(popustDTO.getPopustOpis().length() < 2)
            return ResponseEntity.badRequest().body("Opis popusta treba biti minimalno duljine 2!");
        if(popustDTO.getPopustQrkod().length() < 5)
            return ResponseEntity.badRequest().body("QR kod popusta treba biti minimalno duljine 5!");
        try {
            LocalDateTime rok = DogadajService.getVrijeme(popustDTO.getPopustRok());
            if(rok.isBefore(LocalDateTime.now()))
                throw new RuntimeException("Datum roka mora biti u budućnosti!");
        } catch(Exception e) {
            if(e.getMessage().startsWith("Datum")) return ResponseEntity.badRequest().body(e.getMessage());
            return ResponseEntity.badRequest().body("Datum i vrijeme mora biti u formatu \"dd.MM.gggg. ss:mm\"!");
        }
        return ResponseEntity.ok("Sve ok!");
    }

    @PostMapping
    public ResponseEntity<Integer> createPopust(@RequestBody @Valid final PopustDTO popustDTO) {
        final Integer createdPopustId = popustService.create(popustDTO);
        return new ResponseEntity<>(createdPopustId, HttpStatus.CREATED);
    }

    // za promijenu zastavice iz false u true pogledajte komentar kod putMapping u ponudaPopust
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
