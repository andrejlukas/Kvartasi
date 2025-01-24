package com.mojkvart.rest;

import com.mojkvart.model.KupacPonudaPopustDTO;
import com.mojkvart.model.PonudaDTO;
import com.mojkvart.model.PonudaResponseDTO;
import com.mojkvart.model.PopustDTO;
import com.mojkvart.model.PopustResponseDTO;
import com.mojkvart.service.KupacPonudaPopustService;
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
@RequestMapping(value = "/api/kupacPonudaPopusts", produces = MediaType.APPLICATION_JSON_VALUE)
public class KupacPonudaPopustResource {

    private final KupacPonudaPopustService kupacPonudaPopustService;

    public KupacPonudaPopustResource(
            final KupacPonudaPopustService kupacPonudaPopustService) {
        this.kupacPonudaPopustService = kupacPonudaPopustService;
    }

    @GetMapping
    public ResponseEntity<List<KupacPonudaPopustDTO>> getAllkupacPonudaPopusts() {
        return ResponseEntity.ok(kupacPonudaPopustService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KupacPonudaPopustDTO> getkupacPonudaPopust(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(kupacPonudaPopustService.get(id));
    }

    // Sljedeći API-ji uz to što vraćaju sve informacije o ponudi/popustu, vraćaju
    // i KupacPonudaPopustId kako bi lako mogli mijenjati ponude i popuste iz neiskoristenih u iskoristene
    
    // API za dohvat svih spremljenih neiskoristenih ponuda
    @GetMapping("/ponude/neiskoristene/{kupacId}")
    public ResponseEntity<List<PonudaResponseDTO>> getAllUnusedPonudasForKupac(@PathVariable Integer kupacId) {
        List<PonudaResponseDTO> ponude = kupacPonudaPopustService.findAllUnusedPonudasForKupac(kupacId);
        return ResponseEntity.ok(ponude);
    }

    // API za dohvat svih spremljenih iskoristeih ponuda
    @GetMapping("/ponude/iskoristene/{kupacId}")
    public ResponseEntity<List<PonudaResponseDTO>> getAllUsedPonudasForKupac(@PathVariable Integer kupacId) {
        List<PonudaResponseDTO> ponude = kupacPonudaPopustService.findAllUsedPonudasForKupac(kupacId);
        return ResponseEntity.ok(ponude);
    }

    // API za dohvat svih spremljenih neiskoristenih popusta
    @GetMapping("/popusti/neiskoristeni/{kupacId}")
    public ResponseEntity<List<PopustResponseDTO>> getAllUnusedPopustsForKupac(@PathVariable Integer kupacId) {
        List<PopustResponseDTO> popusti = kupacPonudaPopustService.findAllUnusedPopustsForKupac(kupacId);
        return ResponseEntity.ok(popusti);
    }

    // API za dohvat svih spremljenih iskoristenih ponuda
    @GetMapping("/popusti/iskoristeni/{kupacId}")
    public ResponseEntity<List<PopustResponseDTO>> getAllUsedPopustsForKupac(@PathVariable Integer kupacId) {
        List<PopustResponseDTO> popusti = kupacPonudaPopustService.findAllUsedPopustsForKupac(kupacId);
        return ResponseEntity.ok(popusti);
    }


    // API za laganu promjenu ponude i popusta iz "iskoristeno" u "neiskoristeno" stanje
    @PutMapping("/stanje/{kupacPonudaPopustId}")
    public ResponseEntity<Void> promijeniZastavicu(
            @PathVariable(name = "kupacPonudaPopustId") final Integer kupacPonudaPopustId,
            @RequestBody Map<String, Boolean> requestBody) {
            Boolean novoStanje = requestBody.get("novoStanje");
        kupacPonudaPopustService.promijeniIskoristeno(kupacPonudaPopustId, novoStanje);
        return ResponseEntity.ok().build();
    }

    //UC19, koristite api/kupacPonudaPopusts za spremanje ponude i popusta
    @PostMapping
    public ResponseEntity<Long> createkupacPonudaPopust(
            @RequestBody @Valid final KupacPonudaPopustDTO kupacPonudaPopustDTO) {
        final Long createdId = kupacPonudaPopustService.create(kupacPonudaPopustDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatekupacPonudaPopust(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final KupacPonudaPopustDTO kupacPonudaPopustDTO) {
        kupacPonudaPopustService.update(id, kupacPonudaPopustDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletekupacPonudaPopust(
            @PathVariable(name = "id") final Long id) {
        kupacPonudaPopustService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
