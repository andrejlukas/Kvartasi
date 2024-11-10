package com.mojkvart.rest;

import com.mojkvart.model.KupacDTO;
import com.mojkvart.model.LoginDTO;
import com.mojkvart.model.Response;
import com.mojkvart.service.AdministratorService;
import com.mojkvart.service.KupacService;
import com.mojkvart.service.ModeratorService;
import com.mojkvart.service.TrgovinaService;
import com.mojkvart.util.ReferencedException;
import com.mojkvart.util.ReferencedWarning;
import com.mojkvart.util.jwtUtil;
import jakarta.validation.Valid;
import java.util.List;

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
    @PostMapping
    public ResponseEntity<Object> createKupac(@RequestBody @Valid final KupacDTO kupacDTO) {
        kupacDTO.setKupacSifra(passwordEncoder.encode(kupacDTO.getKupacSifra()));
        kupacService.create(kupacDTO);

        String token = jwtUtil.generateToken(kupacDTO.getKupacEmail(), "KUPAC");
        Response resp = new Response(token, "KUPAC");
        return ResponseEntity.ok().body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> checkIfUserExists(@RequestBody @Valid LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String sifra = loginDTO.getSifra();
        String sifraIzBaze, role = "";

        if (administratorService.findAll().stream().anyMatch(a -> a.getAdministratorEmail().equals(email))) {
            role = "ADMIN";
            sifraIzBaze = administratorService.findAll().stream().filter(a -> a.getAdministratorEmail().equals(email)).findFirst().get().getAdministratorSifra();
        } else if (moderatorService.findAll().stream().anyMatch(m -> m.getModeratorEmail().equals(email))) {
            role = "MODERATOR";
            sifraIzBaze = moderatorService.findAll().stream().filter(m -> m.getModeratorEmail().equals(email)).findFirst().get().getModeratorSifra();
        } else if (trgovinaService.findAll().stream().anyMatch(v -> v.getTrgovinaEmail().equals(email))) {
            role = "VLASNIK";
            sifraIzBaze = trgovinaService.findAll().stream().filter(v -> v.getTrgovinaEmail().equals(email)).findFirst().get().getTrgovinaSifra();
        } else if (kupacService.findAll().stream().anyMatch(k -> k.getKupacEmail().equals(email))) {
            role = "KUPAC";
            sifraIzBaze = kupacService.findAll().stream().filter(k -> k.getKupacEmail().equals(email)).findFirst().get().getKupacSifra();
        } else
            return ResponseEntity.badRequest().body("Nepostojeći e-mail!");

        if (passwordEncoder.matches(sifra, sifraIzBaze)){
            String token = jwtUtil.generateToken(email, role);
            Response resp = new Response(token, role);
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
