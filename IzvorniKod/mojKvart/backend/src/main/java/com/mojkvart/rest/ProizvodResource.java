package com.mojkvart.rest;

import com.mojkvart.model.ProizvodDTO;
import com.mojkvart.service.ProizvodService;
import com.mojkvart.util.ReferencedException;
import com.mojkvart.util.ReferencedWarning;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

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

    // za dohvacanje svih proizvoda potvrdenih od strane moderatora, koristi se kada kupac nije izabrao nikakve filtre
    @GetMapping("/approved")
    public ResponseEntity<List<ProizvodDTO>> getApprovedProizvods() {
        return ResponseEntity.ok(proizvodService.findAllApproved());
    }

    // za dohvacanje svih proizvoda koji jos nisu potvrdeni od strane moderatora
    @GetMapping("/notApproved")
    public ResponseEntity<List<ProizvodDTO>> getNotApprovedProizvods() {
        return ResponseEntity.ok(proizvodService.findAllNotApproved());
    }

    // za dohvacanje svih proizvoda jedne trgovine
    @GetMapping("/approved/{trgovinaId}")
    public ResponseEntity<List<ProizvodDTO>> getTrgovinaApprovedProizvods(
            @PathVariable(name = "trgovinaId") Integer trgovinaId) {
        return ResponseEntity.ok(proizvodService.findApprovedByTrgovina(trgovinaId));
    }
    @GetMapping("/notApproved/{trgovinaId}")
    public ResponseEntity<List<ProizvodDTO>> getTrgovinaNotApprovedProizvods(
            @PathVariable(name = "trgovinaId") Integer trgovinaId) {
        return ResponseEntity.ok(proizvodService.findNotApprovedByTrgovina(trgovinaId));
    }
    @GetMapping("/rejected/{trgovinaId}")
    public ResponseEntity<List<ProizvodDTO>> getTrgovinaRejectedProizvods(
            @PathVariable(name = "trgovinaId") Integer trgovinaId) {
        return ResponseEntity.ok(proizvodService.findRejectedByTrgovina(trgovinaId));
    }

    // to promjeniti u samo potvrdene proizvode kasnije
    @GetMapping("/getBySearch/{input}")
    public ResponseEntity<List<ProizvodDTO>> getSearchedProizvods(@PathVariable(name = "input") String input) {
        return ResponseEntity.ok(proizvodService.getAllTrgovinasBySearch(input));
    }

    // UC11, koristite api/proizvods te pošaljite JSON objekt za kriranje novog
    // proizvoda od strane trgovine
    @PostMapping
    public ResponseEntity<String> createProizvod(@RequestBody @Valid final ProizvodDTO proizvodDTO) {
        if(proizvodDTO.getProizvodNaziv().length() < 2)
            return ResponseEntity.badRequest().body("Naziv proizvoda mora biti minimalno duljine 2!");
        if(proizvodDTO.getProizvodOpis().length() < 10)
            return ResponseEntity.badRequest().body("Opis proizvoda mora biti minimalno duljine 10!");
        if(proizvodDTO.getProizvodCijena() <= 0)
            return ResponseEntity.badRequest().body("Cijena proizvoda mora biti pozitivna!");
        if(proizvodDTO.getProizvodKategorija().length() < 2)
            return ResponseEntity.badRequest().body("Kategorija proizvoda mora biti minimalno duljine 2!");
        if(proizvodDTO.getProizvodSlika().isEmpty())
            return ResponseEntity.badRequest().body("Naziv proizvoda ne smije biti prazan!");
        proizvodService.create(proizvodDTO); // evenutalno zaokružiti cijenu na 2 decimale
        return new ResponseEntity<>("Kreiran proizvod za odobrenje!", HttpStatus.CREATED);
    }

    @PutMapping("/{proizvodId}")
    public ResponseEntity<String> updateProizvod(
            @PathVariable(name = "proizvodId") final Integer proizvodId,
            @RequestBody @Valid final ProizvodDTO proizvodDTO) {
        if(proizvodDTO.getProizvodNaziv().length() < 2)
            return ResponseEntity.badRequest().body("Naziv proizvoda mora biti minimalno duljine 2!");
        if(proizvodDTO.getProizvodOpis().length() < 10)
            return ResponseEntity.badRequest().body("Opis proizvoda mora biti minimalno duljine 10!");
        if(proizvodDTO.getProizvodCijena() <= 0)
            return ResponseEntity.badRequest().body("Cijena proizvoda mora biti pozitivna!");
        if(proizvodDTO.getProizvodKategorija().length() < 2)
            return ResponseEntity.badRequest().body("Kategorija proizvoda mora biti minimalno duljine 2!");
        if(proizvodDTO.getProizvodSlika().isEmpty())
            return ResponseEntity.badRequest().body("Naziv proizvoda ne smije biti prazan!");
        proizvodService.update(proizvodId, proizvodDTO);
        return ResponseEntity.ok("Uspješno promijenjen proizvod!");
    }

    // UC7, koristite za mijenjanje zastavice proizvoda kada ga moderator odobri
    @PutMapping("/stanje/{proizvodId}")
    public ResponseEntity<Void> promijeniZastavicu(
            @PathVariable(name = "proizvodId") final Integer proizvodId,
            @RequestBody Map<String, String> requestBody) {
        String novoStanje = requestBody.get("novoStanje");
        proizvodService.promijeniZastavicu(proizvodId, novoStanje);
        return ResponseEntity.ok().build();
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
