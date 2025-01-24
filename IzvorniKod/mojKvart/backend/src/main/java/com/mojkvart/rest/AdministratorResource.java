package com.mojkvart.rest;

import com.mojkvart.model.AdministratorDTO;
import com.mojkvart.service.AdministratorService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

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
@RequestMapping(value = "/api/administrators", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministratorResource {

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<AdministratorDTO>> getAllAdministrators() {
        return ResponseEntity.ok(administratorService.findAll());
    }

    @GetMapping("/id/{administratorId}")
    public ResponseEntity<AdministratorDTO> getAdministratorById(
            @PathVariable(name = "administratorId") final Integer administratorId) {
        return ResponseEntity.ok(administratorService.get(administratorId));
    }

    @GetMapping("/mail/{administratorEmail}")
    public ResponseEntity<Optional<AdministratorDTO>> getAdministratorByEmail(
            @PathVariable(name = "administratorEmail") final String administratorEmail) {
        return ResponseEntity.ok(administratorService.findByAdministratorEmail(administratorEmail));
    }


    @PostMapping
    public ResponseEntity<Integer> createAdministrator(
            @RequestBody @Valid final AdministratorDTO administratorDTO) {
        administratorDTO.setAdministratorSifra(passwordEncoder.encode(administratorDTO.getAdministratorSifra()));
        final Integer createdAdministratorId = administratorService.create(administratorDTO);
        return new ResponseEntity<>(createdAdministratorId, HttpStatus.CREATED);
    }

    @PutMapping("/{administratorId}")
    public ResponseEntity<Integer> updateAdministrator(
            @PathVariable(name = "administratorId") final Integer administratorId,
            @RequestBody @Valid final AdministratorDTO administratorDTO) {
        administratorService.update(administratorId, administratorDTO);
        return ResponseEntity.ok(administratorId);
    }

    @DeleteMapping("/{administratorId}")
    public ResponseEntity<Void> deleteAdministrator(
            @PathVariable(name = "administratorId") final Integer administratorId) {
        administratorService.delete(administratorId);
        return ResponseEntity.noContent().build();
    }

}