package com.mojkvart.rest;

import com.mojkvart.model.OneLineDTO;
import com.mojkvart.service.JwtService;
import com.mojkvart.util.TokenResponse;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenResource {

    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<TokenResponse> extractInfoFromToken(@RequestBody @Valid OneLineDTO oneLineDTO) {
        String email = jwtService.getEmailFromToken(oneLineDTO.getOneLiner());
        Claims claims = jwtService.getAllClaims(oneLineDTO.getOneLiner());
        String role = (String) claims.get("role");
        TokenResponse tokenResponse = new TokenResponse(email, role);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/expiration")
    public ResponseEntity<Boolean> checkTokenExpiration(@RequestBody @Valid OneLineDTO oneLineDTO) {
        return ResponseEntity.ok(jwtService.isTokenExpired(oneLineDTO.getOneLiner()));
    }
}