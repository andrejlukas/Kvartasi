package com.mojkvart.rest;

import com.mojkvart.model.KupacProizvodDTO;
import com.mojkvart.model.KupacProizvodInfoDTO;
import com.mojkvart.service.KupacProizvodService;
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
@RequestMapping(value = "/api/kupacProizvods", produces = MediaType.APPLICATION_JSON_VALUE)
public class KupacProizvodResource {

    private final KupacProizvodService kupacProizvodService;

    public KupacProizvodResource(
            final KupacProizvodService kupacProizvodService) {
        this.kupacProizvodService = kupacProizvodService;
    }

    // dohvati kosaricu za kupca s odredenim ID-jem
    @GetMapping("/kosarica/{kupacId}")
    public ResponseEntity<Map<Long, List<KupacProizvodInfoDTO>>> getKupacKosarica(@PathVariable Integer kupacId) {
        Map<Long, List<KupacProizvodInfoDTO>> groupedProizvodi = kupacProizvodService.getKupacKosarica(kupacId);
        return ResponseEntity.ok(groupedProizvodi);
    }

    // dohvati prosle narudzbe za kupca s odredenim ID-jem
    @GetMapping("/prosleNarudzbe/{kupacId}")
    public ResponseEntity<Map<Long, List<KupacProizvodInfoDTO>>> getKupacProsleNarudzbe(@PathVariable Integer kupacId) {
        Map<Long, List<KupacProizvodInfoDTO>> groupedProizvodi = kupacProizvodService.getKupacProsleNarudzbe(kupacId);
        return ResponseEntity.ok(groupedProizvodi);
    }

    // dohvati prosle narudzbe za kupca s odredenim ID-jem
    @GetMapping("/narudzbeTrgovina/{kupacId}")
    public ResponseEntity<Map<String, List<KupacProizvodInfoDTO>>> getNarudzbeTrgovina(@PathVariable Integer kupacId) {
        Map<String, List<KupacProizvodInfoDTO>> groupedProizvodi = kupacProizvodService.getTrgovinaNarudzbe(kupacId);
        return ResponseEntity.ok(groupedProizvodi);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KupacProizvodDTO> getkupacProizvod(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(kupacProizvodService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createkupacProizvod(
            @RequestBody @Valid final KupacProizvodDTO kupacProizvodDTO) {
        final Long createdId = kupacProizvodService.create(kupacProizvodDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatekupacProizvod(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final KupacProizvodDTO kupacProizvodDTO) {
        kupacProizvodService.update(id, kupacProizvodDTO);
        return ResponseEntity.ok(id);
    }

    //Za testiranje neimplementiranih funkcije
    @PostMapping("/kosarica/{kupacId}/kupi")
    public ResponseEntity<String> kupiProizvodeIzKosarice(@PathVariable Integer kupacId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body("Funkcionalnost kupovine proizvoda iz kosarice trenutno nije implementirana.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletekupacProizvod(
            @PathVariable(name = "id") final Long id) {
        kupacProizvodService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
