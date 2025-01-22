package com.mojkvart.rest;

import com.mojkvart.model.KupacDTO;
import com.mojkvart.model.LoginDTO;
import com.mojkvart.model.OneLineDTO;
import com.mojkvart.model.VerificationDTO;
import com.mojkvart.util.AuthenticationResponse;
import com.mojkvart.service.*;
import com.mojkvart.util.NotFoundException;
import com.mojkvart.util.ReferencedException;
import com.mojkvart.util.ReferencedWarning;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
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

    @Autowired
    private EmailService mailService;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9šđčćžŠĐČĆŽ._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private String generateCode() {
        StringBuilder code = new StringBuilder();
        for(int i = 0; i < 6; i++) code.append(Double.toString(Math.floor(Math.random() * 10)).charAt(0));
        return code.toString();
    }

    private void verificationRoutine(KupacDTO kupacDTO) {
        String verificationCode = generateCode();
        kupacDTO.setKupacSifra(passwordEncoder.encode(kupacDTO.getKupacSifra()));
        kupacDTO.setVerifikacijskiKod(verificationCode);
        kupacDTO.setKodValidanDo(LocalDateTime.now().plusMinutes(5)); // verifikacijski kod traje 5 minuta
        kupacDTO.setKupacStatus("N");
    }


    @GetMapping
    public ResponseEntity<List<KupacDTO>> getAllKupacs() {
        return ResponseEntity.ok(kupacService.findAll());
    }

    // API za dohvacanje svih suspendiranih kupaca
    @GetMapping("/suspendirani")
    public List<KupacDTO> getSuspendedCustomers() {
        return kupacService.getSuspendedCustomers();
    }

    // API za dohvacanje svih verificiranih kupaca
    @GetMapping("/verificirani")
    public List<KupacDTO> getVerifiedCustomers() {
        return kupacService.getVerifiedCustomers();
    }

    // API za dohvacanje svih neverificiranih kupaca
    @GetMapping("/neverificirani")
    public List<KupacDTO> getUnverifiedCustomers() {
        return kupacService.getUnverifiedCustomers();
    }

    @GetMapping("/{kupacEmail}")
    public ResponseEntity<KupacDTO> getKupac(@PathVariable(name = "kupacEmail") final String kupacEmail) {
        if(kupacService.findByKupacEmail(kupacEmail).isEmpty())
            throw new NotFoundException("Nepostojeći kupac!");
        return ResponseEntity.ok(kupacService.findByKupacEmail(kupacEmail).get());
    }

    @PostMapping("/signup")
    public ResponseEntity<String> createKupac(@RequestBody @Valid final KupacDTO kupacDTO) {
        if(kupacDTO.getKupacIme().length() < 2)
            return ResponseEntity.badRequest().body("Ime mora biti minimalno duljine 2 znaka!");
        if(kupacDTO.getKupacPrezime().length() < 2)
            return ResponseEntity.badRequest().body("Prezime mora biti minimalno duljine 2 znaka!");
        if(kupacDTO.getKupacSifra().isEmpty())
            return ResponseEntity.badRequest().body("Popunite polje 'Kućna adresa'!");
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(kupacDTO.getKupacEmail());
        if(!matcher.matches())
            return ResponseEntity.badRequest().body("Upisan nevažeći oblik e-mail adrese!");
        if(kupacDTO.getKupacSifra().length() < 8)
            return ResponseEntity.badRequest().body("Vaša lozinka mora biti minimalno duljine 8 znakova!");
        if(administratorService.findByAdministratorEmail(kupacDTO.getKupacEmail()).isPresent() ||
                moderatorService.findByModeratorEmail(kupacDTO.getKupacEmail()).isPresent() ||
                trgovinaService.findByTrgovinaEmail(kupacDTO.getKupacEmail()).isPresent() ||
                kupacService.findByKupacEmail(kupacDTO.getKupacEmail()).isPresent() &&
                kupacService.findByKupacEmail(kupacDTO.getKupacEmail()).get().getKupacStatus().equals("V")){
            return ResponseEntity.badRequest().body("Imate već postojeći korisnički račun?");
        }

        if(kupacService.findByKupacEmail(kupacDTO.getKupacEmail()).isEmpty()) {
            verificationRoutine(kupacDTO);
            kupacService.create(kupacDTO);
        } else {
            Integer kupacId = kupacService.findByKupacEmail(kupacDTO.getKupacEmail()).get().getKupacId();
            verificationRoutine(kupacDTO);
            kupacService.update(kupacId, kupacDTO);
        }
        return new ResponseEntity<>("Stvoren kupac!", HttpStatus.CREATED);
    }

    @PostMapping("/sendVerificationMail")
    public ResponseEntity<String> sendVerificationMail(@RequestBody final OneLineDTO oneLineDTO) {
        KupacDTO kupacDTO = kupacService.findByKupacEmail(oneLineDTO.getOneLiner()).get();
        mailService.sendVerificationMail(oneLineDTO.getOneLiner(), kupacDTO.getVerifikacijskiKod());
        kupacDTO.setVerifikacijskiKod(passwordEncoder.encode(kupacDTO.getVerifikacijskiKod()));
        kupacService.update(kupacDTO.getKupacId(), kupacDTO);
        return ResponseEntity.ok("Verifikacijski mail poslan!");
    }

    @PostMapping("/verification")
    public ResponseEntity<Object> verifyKupac(@RequestBody @Valid final VerificationDTO verificationDTO) {
        KupacDTO kupacDTO = new KupacDTO();
        if(kupacService.findByKupacEmail(verificationDTO.getEmail()).isPresent())
            kupacDTO = kupacService.findByKupacEmail(verificationDTO.getEmail()).get();
        else
            return ResponseEntity.badRequest().body("Pričekaj da mail dođe!");

        if(kupacDTO.getKodValidanDo().isBefore(LocalDateTime.now()))
            return ResponseEntity.badRequest().body("Vrijeme za verifikaciju je isteklo.\nPokušajte se ponovno registrirati!");
        if(!passwordEncoder.matches(verificationDTO.getVerificationCode(), kupacService.get(kupacDTO.getKupacId()).getVerifikacijskiKod()))
            return ResponseEntity.badRequest().body("Unesen neispravan verifikacijski kod.\nPokušajte ga ponovno upisati!");

        kupacDTO.setVerifikacijskiKod(null);
        kupacDTO.setKodValidanDo(null);
        kupacDTO.setKupacStatus("V");
        updateKupac(kupacDTO.getKupacId(), kupacDTO);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "KUPAC");
        AuthenticationResponse resp = new AuthenticationResponse(jwtService.generateToken(claims, kupacDTO.getKupacEmail()), "KUPAC");
        return ResponseEntity.ok().body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginKupac(@RequestBody @Valid LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String sifra = loginDTO.getSifra();
        String sifraIzBaze, role = "";

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches())
            return ResponseEntity.badRequest().body("Upisan nevažeći oblik e-mail adrese!");
        if(sifra.length() < 8)
            return ResponseEntity.badRequest().body("Lozinka mora biti minimalno duljine 8 znakova!");

        if (administratorService.findByAdministratorEmail(email).isPresent()) {
            role = "ADMINISTRATOR";
            sifraIzBaze = administratorService.findByAdministratorEmail(email).get().getAdministratorSifra();
        } else if (moderatorService.findByModeratorEmail(email).isPresent()) {
            role = "MODERATOR";
            sifraIzBaze = moderatorService.findByModeratorEmail(email).get().getModeratorSifra();
        } else if (trgovinaService.findByTrgovinaEmail(email).isPresent()) {
            role = "TRGOVINA";
            sifraIzBaze = trgovinaService.findByTrgovinaEmail(email).get().getTrgovinaSifra();
        } else if (kupacService.findByKupacEmail(email).isPresent() &&
                   kupacService.findByKupacEmail(email).get().getKupacStatus().equals("V")) {
            role = "KUPAC";
            sifraIzBaze = kupacService.findByKupacEmail(email).get().getKupacSifra();
        } else
            return ResponseEntity.badRequest().body("Unesena kriva lozinka ili e-mail adresa!");

        if(sifraIzBaze == null)
            return ResponseEntity.badRequest().body("Registrirani ste s Google računom!");


        if (passwordEncoder.matches(sifra, sifraIzBaze)){
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", role);
            AuthenticationResponse resp = new AuthenticationResponse(jwtService.generateToken(claims, email), role);
            return ResponseEntity.ok().body(resp);
        }
        else
            return ResponseEntity.badRequest().body("Unesena kriva lozinka ili e-mail adresa!");
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