package com.mojkvart.rest;

import com.mojkvart.model.OneLineDTO;
import com.mojkvart.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/mail", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<String> sendMail(@RequestBody OneLineDTO oneLineDTO) {
        emailService.sendVerificationMail(oneLineDTO.getOneLiner(), "123456");
        return ResponseEntity.ok("Sve je ok.");
    }
}
