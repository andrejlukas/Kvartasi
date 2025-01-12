package com.mojkvart.rest;

import com.mojkvart.model.TrgovinaDTO;
import com.mojkvart.service.*;
import com.mojkvart.util.AuthenticationResponse;
import com.mojkvart.util.ReferencedException;
import com.mojkvart.util.ReferencedWarning;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/trgovinas", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrgovinaResource {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9šđčćžŠĐČĆŽ._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Autowired
    private KupacService kupacService;

    @Autowired
    private TrgovinaService trgovinaService;

    @Autowired
    private ModeratorService moderatorService;

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private ProizvodService proizvodService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping
    public ResponseEntity<List<TrgovinaDTO>> getAllTrgovinas() {
        return ResponseEntity.ok(trgovinaService.findAll());
    }

    //UC4, koristite api/trgovinas/{trgovinaEmail} za pregled osnovnih podataka
    @GetMapping("/{trgovinaEmail}")
    public ResponseEntity<Object> getTrgovina(@PathVariable(name = "trgovinaEmail") final String trgovinaEmail) {
        if(trgovinaService.findByTrgovinaEmail(trgovinaEmail).isEmpty())
            return ResponseEntity.badRequest().body("ne funkcionira getTrgovina");
        return ResponseEntity.ok(trgovinaService.findByTrgovinaEmail(trgovinaEmail).get());
    }

    @GetMapping("/getById/{trgovinaId}")
    public ResponseEntity<Object> getTrgovina(@PathVariable(name = "trgovinaId") final Integer trgovinaId) {
        return ResponseEntity.ok(trgovinaService.get(trgovinaId));
    }

    //UC5, koristite api/trgovinas za kreiranje nove trgovine
    @PostMapping("/signup")
    public ResponseEntity<Object> createTrgovina(@RequestBody @Valid final TrgovinaDTO trgovinaDTO) {
        if(trgovinaDTO.getTrgovinaNaziv().length() < 2)
            return ResponseEntity.badRequest().body("Naziv trgovine mora biti minimalno duljine 2 znaka!");
        if(trgovinaDTO.getTrgovinaOpis().length() < 10)
            return ResponseEntity.badRequest().body("Opis trgovine mora biti minimalno duljine 10 znakova!");
        if(trgovinaDTO.getTrgovinaKategorija().length() < 2)
            return ResponseEntity.badRequest().body("Kategorija trgovine mora biti minimalno duljine 2 znaka!");
        if(trgovinaDTO.getTrgovinaLokacija().isEmpty())
            return ResponseEntity.badRequest().body("Morate odabrati lokaciju trgovine na karti!");
        if(trgovinaDTO.getTrgovinaSlika().isEmpty())
            return ResponseEntity.badRequest().body("Morate upisati URL slike!");
        if(trgovinaDTO.getTrgovinaRadnoVrijemeOd().isEmpty() || trgovinaDTO.getTrgovinaRadnoVrijemeDo().isEmpty())
            return ResponseEntity.badRequest().body("Početno radno vrijeme treba biti upisano!");
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(trgovinaDTO.getTrgovinaEmail());
        if(!matcher.matches())
            return ResponseEntity.badRequest().body("Upisan nevažeći oblik e-mail adrese!");
        if(trgovinaDTO.getTrgovinaSifra().length() < 8)
            return ResponseEntity.badRequest().body("Lozinka mora biti minimalno duljine 8 znakova!");
        if(administratorService.findByAdministratorEmail(trgovinaDTO.getTrgovinaEmail()).isPresent() ||
                moderatorService.findByModeratorEmail(trgovinaDTO.getTrgovinaEmail()).isPresent() ||
                trgovinaService.findByTrgovinaEmail(trgovinaDTO.getTrgovinaEmail()).isPresent() ||
                kupacService.findByKupacEmail(trgovinaDTO.getTrgovinaEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Imate već postojeći korisnički račun?");
        }

        trgovinaDTO.setTrgovinaSifra(passwordEncoder.encode(trgovinaDTO.getTrgovinaSifra()));
        trgovinaService.create(trgovinaDTO);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "TRGOVINA");
        AuthenticationResponse resp = new AuthenticationResponse(jwtService.generateToken(claims, trgovinaDTO.getTrgovinaEmail()), "TRGOVINA");
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    //UC4, koristite api/trgovinas/{trgovinaId} za uredivanje osnovnih podataka
    @PutMapping("/{trgovinaId}")
    public ResponseEntity<Integer> updateTrgovina(
            @PathVariable(name = "trgovinaId") final Integer trgovinaId,
            @RequestBody @Valid final TrgovinaDTO trgovinaDTO) {
        trgovinaService.update(trgovinaId, trgovinaDTO);
        return ResponseEntity.ok(trgovinaId);
    }

    //UC6, koristite api/trgovinas/{trgovinaId} za brisanje trgovine iz sustava
    @DeleteMapping("/{trgovinaId}")
    public ResponseEntity<Void> deleteTrgovina(
            @PathVariable(name = "trgovinaId") final Integer trgovinaId) {
        final ReferencedWarning referencedWarning = trgovinaService.getReferencedWarning(trgovinaId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        trgovinaService.delete(trgovinaId);
        return ResponseEntity.noContent().build();
    }

}
