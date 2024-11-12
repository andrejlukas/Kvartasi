package com.mojkvart.rest;

import com.mojkvart.model.KupacDTO;
import com.mojkvart.model.LoginDTO;
import com.mojkvart.util.AuthenticationResponse;
import com.mojkvart.service.*;
import com.mojkvart.util.ReferencedException;
import com.mojkvart.util.ReferencedWarning;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/kupacs", produces = MediaType.APPLICATION_JSON_VALUE)
public class KupacResource {

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private ModeratorService moderatorService;

    @Autowired
    private TrgovinaService trgovinaService;

    @Autowired
    private KupacService kupacService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    private static final String NAME_SURNAME_FORMAT = "^[A-ZČĆŽĐŠÁÉÍÓÚÑÇÀÈÌÒÙÄËÏÖÜÝŸÆŒ][a-zčćžđšáéíóúñçàèìòùäëïöüýÿæœ' -]{1,}$";
    private static final String EMAIL_FORMAT = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_STRENGTH = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}";

   
    @GetMapping
    public ResponseEntity<List<KupacDTO>> getAllKupacs() {
        return ResponseEntity.ok(kupacService.findAll());
    }

    //UC3, koristite api/kupacs/{kupacId} za dohvaćanje osobnih podataka
    @GetMapping("/{kupacId}")
    public ResponseEntity<KupacDTO> getKupac(@PathVariable(name = "kupacId") final Integer kupacId) {
        return ResponseEntity.ok(kupacService.get(kupacId));
    }

    //UC1, koristite api/kupacs i pošaljite JSON objekt za registraciju
    @PostMapping("/signup")
    public ResponseEntity<Object> createKupac(@RequestBody @Valid final KupacDTO kupacDTO) {
        if(administratorService.findByAdministratorEmail(kupacDTO.getKupacEmail()).isPresent() ||
           moderatorService.findByModeratorEmail(kupacDTO.getKupacEmail()).isPresent() ||
           trgovinaService.findByTrgovinaEmail(kupacDTO.getKupacEmail()).isPresent() ||
           kupacService.findByKupacEmail(kupacDTO.getKupacEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Već postojeći email!");
        }
        kupacDTO.setKupacSifra(passwordEncoder.encode(kupacDTO.getKupacSifra()));
        Integer kupacId = kupacService.create(kupacDTO);

        // trebam kasnije kao i u loginu provjerit sva 4 slucaja
        Map<String, Object> claims = new HashMap<>(); // u iducoj verziji staviti u generateToken
        claims.put("id", kupacId);
        claims.put("role", "KUPAC");
        AuthenticationResponse resp = new AuthenticationResponse(jwtService.generateToken(kupacDTO.getKupacEmail()), kupacId, "KUPAC");
        return ResponseEntity.ok().body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody @Valid LoginDTO loginDTO) {
        Integer id = -1;
        String email = loginDTO.getEmail();
        String sifra = loginDTO.getSifra();
        String sifraIzBaze, role = "";

        if (administratorService.findByAdministratorEmail(email).isPresent()) {
            id = administratorService.findByAdministratorEmail(email).get().getAdministratorId();
            role = "ADMINISTRATOR";
            sifraIzBaze = administratorService.findByAdministratorEmail(email).get().getAdministratorSifra();
        } else if (moderatorService.findByModeratorEmail(email).isPresent()) {
            id = moderatorService.findByModeratorEmail(email).get().getModeratorId();
            role = "MODERATOR";
            sifraIzBaze = moderatorService.findByModeratorEmail(email).get().getModeratorSifra();
        } else if (trgovinaService.findByTrgovinaEmail(email).isPresent()) {
            id = trgovinaService.findByTrgovinaEmail(email).get().getTrgovinaId();
            role = "TRGOVINA";
            sifraIzBaze = trgovinaService.findByTrgovinaEmail(email).get().getTrgovinaSifra();
        } else if (kupacService.findByKupacEmail(email).isPresent()) {
            id = kupacService.findByKupacEmail(email).get().getKupacId();
            role = "KUPAC";
            sifraIzBaze = kupacService.findByKupacEmail(email).get().getKupacSifra();
        } else
            return ResponseEntity.badRequest().body("Nepostojeći e-mail!");


        Map<String, Object> claims = new HashMap<>(); // ovo u iducoj verziji staviti u generate token
        claims.put("id", id);
        claims.put("role", role);
        if (passwordEncoder.matches(sifra, sifraIzBaze)){
            AuthenticationResponse resp = new AuthenticationResponse(jwtService.generateToken(email), id, role);
            return ResponseEntity.ok().body(resp);
        }
        else
            return ResponseEntity.badRequest().body("Kriva lozinka!");
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