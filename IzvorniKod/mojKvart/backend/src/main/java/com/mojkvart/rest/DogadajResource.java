package com.mojkvart.rest;

import com.mojkvart.model.DogadajDTO;
import com.mojkvart.service.DogadajService;
import com.mojkvart.util.ReferencedException;
import com.mojkvart.util.ReferencedWarning;
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
@RequestMapping(value = "/api/dogadajs", produces = MediaType.APPLICATION_JSON_VALUE)
public class DogadajResource {

    private final DogadajService dogadajService;

    public DogadajResource(final DogadajService dogadajService) {
        this.dogadajService = dogadajService;
    }

    @GetMapping("/{dogadajId}")
    public ResponseEntity<DogadajDTO> getDogadaj(@PathVariable(name = "dogadajId") final Integer dogadajId) {
        return ResponseEntity.ok(dogadajService.get(dogadajId));
    }

    //UC10, koristite api/dogadajs za dohvacanje svih dogadaja od strane moderatora
    //UC17, koristite api/dogadajs za dohvacanje svih dogadaja od strane korisnika
    @GetMapping
    public ResponseEntity<List<DogadajDTO>> getAllDogadajs() {
        return ResponseEntity.ok(dogadajService.findAllUpcoming());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<DogadajDTO>> getAllDogadajsFiltered() {
        return ResponseEntity.ok(dogadajService.findAllUpcomingSorted());
    }

    // API za dohvacanje svih nadolazecih dogadaja odredene trgovine
    @GetMapping("/upcoming/{trgovinaId}")
    public ResponseEntity<List<DogadajDTO>> getUpcomingTrgovinasDogadajs(@PathVariable(name = "trgovinaId") final Integer id) {
        return ResponseEntity.ok(dogadajService.getUpcomingTrgovinasDogadajs(id));
    }

    // API za dohvacanje svih prijasnjih dogadaja odredene trgovine
    @GetMapping("/finished/{trgovinaId}")
    public ResponseEntity<List<DogadajDTO>> getFinishedTrgovinasDogadajs(@PathVariable(name = "trgovinaId") final Integer id) {
        return ResponseEntity.ok(dogadajService.getFinishedTrgovinasDogadajs(id));
    }

    //UC12, koristite api/dogadajs i posaljite JSON objekt za kreiranje novog dogadaja od strane trgovine
    @PostMapping
    public ResponseEntity<String> createDogadaj(@RequestBody @Valid final DogadajDTO dogadajDTO) {
        if(dogadajDTO.getDogadajNaziv().length() < 2)
            return ResponseEntity.badRequest().body("Naziv događaja mora biti minimalno duljine 2!");
        if(dogadajDTO.getDogadajOpis().length() < 10)
            return ResponseEntity.badRequest().body("Opis događaja mora biti minimalno duljine 10!");
        try {
            LocalDateTime pocetakDogadaja = DogadajService.getVrijeme(dogadajDTO.getDogadajPocetak());
            LocalDateTime krajDogadaja = DogadajService.getVrijeme(dogadajDTO.getDogadajKraj());
            if(pocetakDogadaja.isBefore(LocalDateTime.now()))
                throw new RuntimeException("Datum početka događaja mora biti u budućnosti!");
            if(krajDogadaja.isBefore(LocalDateTime.now()))
                throw new RuntimeException("Datum kraja događaja mora biti u budućnosti!");
            if(krajDogadaja.isBefore(pocetakDogadaja))
                throw new RuntimeException("Datum kraja mora biti poslije datuma početka događaja!");
        } catch(Exception e) {
            if(e.getMessage().startsWith("Datum")) return ResponseEntity.badRequest().body(e.getMessage());
            return ResponseEntity.badRequest().body("Datum i vrijeme događaja mora biti u formatu \"dd.MM.gggg. ss:mm\"!");
        }
        if(dogadajDTO.getDogadajSlika().isEmpty())
            return ResponseEntity.badRequest().body("URL slike proizvoda ne smije biti prazan!");

        dogadajService.create(dogadajDTO);
        return new ResponseEntity<>("Uspješno kreiran događaj.", HttpStatus.CREATED);
    }

    @PutMapping("/{dogadajId}")
    public ResponseEntity<String> updateDogadaj(@PathVariable(name = "dogadajId") final Integer dogadajId, @RequestBody @Valid final DogadajDTO dogadajDTO) {
        if(dogadajDTO.getDogadajNaziv().length() < 2)
            return ResponseEntity.badRequest().body("Naziv događaja mora biti minimalno duljine 2!");
        if(dogadajDTO.getDogadajOpis().length() < 10)
            return ResponseEntity.badRequest().body("Opis događaja mora biti minimalno duljine 10!");
        try {
            LocalDateTime pocetakDogadaja = DogadajService.getVrijeme(dogadajDTO.getDogadajPocetak());
            LocalDateTime krajDogadaja = DogadajService.getVrijeme(dogadajDTO.getDogadajKraj());
            if(pocetakDogadaja.isBefore(LocalDateTime.now()))
                throw new RuntimeException("Datum početka događaja mora biti u budućnosti!");
            if(krajDogadaja.isBefore(LocalDateTime.now()))
                throw new RuntimeException("Datum kraja događaja mora biti u budućnosti!");
            if(krajDogadaja.isBefore(pocetakDogadaja))
                throw new RuntimeException("Datum kraja mora biti poslije datuma početka događaja!");
        } catch(Exception e) {
            if(e.getMessage().startsWith("Datum")) return ResponseEntity.badRequest().body(e.getMessage());
            return ResponseEntity.badRequest().body("Datum i vrijeme događaja mora biti u formatu \"dd.MM.gggg. ss:mm\"!");
        }
        if(dogadajDTO.getDogadajSlika().isEmpty())
            return ResponseEntity.badRequest().body("URL slike proizvoda ne smije biti prazan!");
        dogadajService.update(dogadajId, dogadajDTO);
        return ResponseEntity.ok("Uspješno ažuriran događaj!");
    }

    //UC10, koristite api/dogadajs/{dogadajId} za brisanje nekog dogadaja od strane moderatora
    @DeleteMapping("/{dogadajId}")
    public ResponseEntity<Void> deleteDogadaj(
            @PathVariable(name = "dogadajId") final Integer dogadajId) {
        final ReferencedWarning referencedWarning = dogadajService.getReferencedWarning(dogadajId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        dogadajService.delete(dogadajId);
        return ResponseEntity.noContent().build();
    }
}