package com.mojkvart.rest;

import com.mojkvart.model.KupacDTO;
import com.mojkvart.service.KupacService;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedException;
import com.mojkvart.util.ReferencedWarning;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/kupacs", produces = MediaType.APPLICATION_JSON_VALUE)
public class KupacResource {

    private final KupacService kupacService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        Optional<KupacDTO> previousKupacDTO = kupacService.findAll().stream().
                filter(k -> k.getKupacEmail().equals(kupacDTO.getKupacEmail())).findFirst();
        if(previousKupacDTO.isPresent()) {
            KupacDTO previousKupac = previousKupacDTO.get();
            if(!passwordEncoder.encode(kupacDTO.getKupacSifra()).
                    equals(previousKupac.getKupacSifra())) {
                throw new NotFoundException("Wrong password!");
            }
            System.out.println("Right Password!");
            return ResponseEntity.ok(previousKupac.getKupacId());
        } else {
            if(kupacDTO.getKupacIme().isEmpty())
                throw new NotFoundException("You don't have an account! Try using Sign up!");
            kupacDTO.setKupacSifra(passwordEncoder.encode(kupacDTO.getKupacSifra()));
            final Integer createdKupacId = kupacService.create(kupacDTO);
            return new ResponseEntity<>(createdKupacId, HttpStatus.CREATED);
        }
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
