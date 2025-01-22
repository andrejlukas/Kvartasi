package com.mojkvart.rest;

import com.mojkvart.model.ModeratorDTO;
import com.mojkvart.service.*;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping(value = "/api/moderators", produces = MediaType.APPLICATION_JSON_VALUE)
public class ModeratorResource {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9šđčćžŠĐČĆŽ._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

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

    @GetMapping
    public ResponseEntity<List<ModeratorDTO>> getAllModerators() {
        return ResponseEntity.ok(moderatorService.findAll());
    }

    @GetMapping("/verified")
    public ResponseEntity<List<ModeratorDTO>> getAllVerifiedModerators() {
        return ResponseEntity.ok(moderatorService.findAllVerified());
    }

    @GetMapping("/notverified")
    public ResponseEntity<List<ModeratorDTO>> getAllNotVerifiedModerators() {
        return ResponseEntity.ok(moderatorService.findAllNotVerified());
    }

    @GetMapping("/suspended")
    public ResponseEntity<List<ModeratorDTO>> getAllSuspendedModerators() {
        return ResponseEntity.ok(moderatorService.findAllSuspended());
    }

    @GetMapping("/{moderatorEmail}")
    public ResponseEntity<ModeratorDTO> getModerator(
            @PathVariable(name = "moderatorEmail") final String moderatorEmail) {
        return ResponseEntity.ok(moderatorService.findByModeratorEmail(moderatorEmail).get());
    }

    //UC5, koristite api/moderators i pošaljite JSON objekt za kreiranje novog moderatora
    @PostMapping
    public ResponseEntity<Object> createModerator(@RequestBody @Valid final ModeratorDTO moderatorDTO) {
        if(moderatorDTO.getModeratorIme().length() < 2)
            return ResponseEntity.badRequest().body("Ime mora biti minimalno duljine 2 znaka!");
        if(moderatorDTO.getModeratorPrezime().length() < 2)
            return ResponseEntity.badRequest().body("Prezime mora biti minimalno duljine 2 znaka!");
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(moderatorDTO.getModeratorEmail());
        if(!matcher.matches())
            return ResponseEntity.badRequest().body("Upisan nevažeći oblik e-mail adrese!");
        if(moderatorDTO.getModeratorSifra().length() < 8)
            return ResponseEntity.badRequest().body("Vaša lozinka mora biti minimalno duljine 8 znakova!");
        if(administratorService.findByAdministratorEmail(moderatorDTO.getModeratorEmail()).isPresent() ||
                (moderatorService.findByModeratorEmail(moderatorDTO.getModeratorEmail()).isPresent() &&
                        moderatorService.findByModeratorEmail(moderatorDTO.getModeratorEmail()).get().getModeratorStatus().equals("V")) ||
                (trgovinaService.findByTrgovinaEmail(moderatorDTO.getModeratorEmail()).isPresent() &&
                        trgovinaService.findByTrgovinaEmail(moderatorDTO.getModeratorEmail()).get().getTrgovinaStatus().equals("V")) ||
                (kupacService.findByKupacEmail(moderatorDTO.getModeratorEmail()).isPresent() &&
                        kupacService.findByKupacEmail(moderatorDTO.getModeratorEmail()).get().getKupacStatus().equals("V"))) {
            return ResponseEntity.badRequest().body("Imate već postojeći korisnički račun?");
        }

        moderatorDTO.setModeratorSifra(passwordEncoder.encode(moderatorDTO.getModeratorSifra()));
        final Integer createdModeratorId = moderatorService.create(moderatorDTO);
        return new ResponseEntity<>(createdModeratorId, HttpStatus.CREATED);
    }

    @PutMapping("/{moderatorId}")
    public ResponseEntity<Integer> updateModerator(
            @PathVariable(name = "moderatorId") final Integer moderatorId,
            @RequestBody @Valid final ModeratorDTO moderatorDTO) {
        moderatorService.update(moderatorId, moderatorDTO);
        return ResponseEntity.ok(moderatorId);
    }

    //UC6, koristite api/moderators/{moderatorId} za brisanje moderatora iz sustava
    @DeleteMapping("/{moderatorId}")
    public ResponseEntity<Void> deleteModerator(
            @PathVariable(name = "moderatorId") final Integer moderatorId) {
        moderatorService.delete(moderatorId);
        return ResponseEntity.noContent().build();
    }

}
