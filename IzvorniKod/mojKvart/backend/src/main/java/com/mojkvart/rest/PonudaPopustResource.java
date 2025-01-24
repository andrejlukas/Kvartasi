package com.mojkvart.rest;

import com.mojkvart.model.PonudaPopustDTO;
import com.mojkvart.service.PonudaPopustService;
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
@RequestMapping(value = "/api/ponudaPopusts", produces = MediaType.APPLICATION_JSON_VALUE)
public class PonudaPopustResource {

    private final PonudaPopustService ponudaPopustService;

    public PonudaPopustResource(final PonudaPopustService ponudaPopustService) {
        this.ponudaPopustService = ponudaPopustService;
    }

    @GetMapping
    public ResponseEntity<List<PonudaPopustDTO>> getAllPonudaPopusts() {
        return ResponseEntity.ok(ponudaPopustService.findAll());
    }

    @GetMapping("/{ponudaPopustId}")
    public ResponseEntity<PonudaPopustDTO> getPonudaPopust(
            @PathVariable(name = "ponudaPopustId") final Integer ponudaPopustId) {
        return ResponseEntity.ok(ponudaPopustService.get(ponudaPopustId));
    }

    //UC13, koristite api/ponudaPopusts i posaljite JSON objekt za kreiranje nove ponudePopusta
    @PostMapping
    public ResponseEntity<Integer> createPonudaPopust(
            @RequestBody @Valid final PonudaPopustDTO ponudaPopustDTO) {
        final Integer createdPonudaPopustId = ponudaPopustService.create(ponudaPopustDTO);
        return new ResponseEntity<>(createdPonudaPopustId, HttpStatus.OK);
    }

    @PutMapping("/{ponudaPopustId}")
    public ResponseEntity<Integer> updatePonudaPopust(
            @PathVariable(name = "ponudaPopustId") final Integer ponudaPopustId,
            @RequestBody @Valid final PonudaPopustDTO ponudaPopustDTO) {
        ponudaPopustService.update(ponudaPopustId, ponudaPopustDTO);
        return ResponseEntity.ok(ponudaPopustId);
    }

    // koristite za mijenjanje zastavice ponude i popusta kada ih moderator odobri,
    // saljete prvo id ponude u get za ponudu (ili popust) i onda iz te ponude
    // uzimate id od ponudePopusta i saljete ovdje
    
    @PutMapping("/stanje/{ponudaPopustId}")
    public ResponseEntity<Void> promijeniZastavicu(
            @PathVariable(name = "ponudaPopustId") final Integer ponudaPopustId,
            @RequestBody Map<String, Boolean> requestBody) {
            Boolean novoStanje = requestBody.get("novoStanje");
        ponudaPopustService.promijeniZastavicu(ponudaPopustId, novoStanje);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{ponudaPopustId}")
    public ResponseEntity<Void> deletePonudaPopust(
            @PathVariable(name = "ponudaPopustId") final Integer ponudaPopustId) {
        final ReferencedWarning referencedWarning = ponudaPopustService.getReferencedWarning(ponudaPopustId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        ponudaPopustService.delete(ponudaPopustId);
        return ResponseEntity.noContent().build();
    }

}
